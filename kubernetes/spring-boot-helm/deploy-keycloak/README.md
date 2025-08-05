
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/kubernetes/spring-boot-helm/deploy-pgadmin$ helm install my-keycloak oci://registry-1.docker.io/bitnamicharts/keycloak
Pulled: registry-1.docker.io/bitnamicharts/keycloak:24.8.1
Digest: sha256:b07d90a81e3b5c23b9e9149d7d99222771a6097e41c68c42c7c0a33461e9d672
NAME: my-keycloak
LAST DEPLOYED: Sun Aug  3 13:33:12 2025
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
CHART NAME: keycloak
CHART VERSION: 24.8.1
APP VERSION: 26.3.2

NOTICE: Starting August 28th, 2025, only a limited subset of images/charts will remain available for free. Backup will be available for some time at the 'Bitnami Legacy' repository. More info at https://github.com/bitnami/containers/issues/83267

** Please be patient while the chart is being deployed **

Keycloak can be accessed through the following DNS name from within your cluster:

    my-keycloak.default.svc.cluster.local (port 80)

To access Keycloak from outside the cluster execute the following commands:

1. Get the Keycloak URL by running these commands:

   export HTTP_SERVICE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[?(@.name=='http')].port}" services my-keycloak)
   kubectl port-forward --namespace default svc/my-keycloak ${HTTP_SERVICE_PORT}:${HTTP_SERVICE_PORT} &

   echo "http://127.0.0.1:${HTTP_SERVICE_PORT}/"

2. Access Keycloak using the obtained URL.

WARNING: There are "resources" sections in the chart not set. Using "resourcesPreset" is not recommended for production. For production installations, please set the following values according to your workload needs:
- resources
  +info https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/



# 

lefort@GT13Pro:/mnt/d/dev/git/dev-blog/kubernetes/spring-boot-helm/deploy-pgadmin$ Unable to listen on port 80: Listeners failed to create with the following errors: [unable to create listener: Error listen tcp4 127.0.0.1:80: bind: permission denied unable to create listener: Error listen tcp6 [::1]:80: bind: permission denied]
error: unable to listen on any of the requested ports: [{80 8080}]
^C
[1]+  Exit 1                  kubectl port-forward --namespace default svc/my-keycloak ${HTTP_SERVICE_PORT}:${HTTP_SERVICE_PORT}
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/kubernetes/spring-boot-helm/deploy-pgadmin$ ^C
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/kubernetes/spring-boot-helm/deploy-pgadmin$ echo ${HTTP_SERVICE_PORT}
80
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/kubernetes/spring-boot-helm/deploy-pgadmin$ kubectl port-forward --namespace default svc/my-keycloak 8282:${HTTP_SERVICE_PORT} &
[1] 661595
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/kubernetes/spring-boot-helm/deploy-pgadmin$ Forwarding from 127.0.0.1:8282 -> 8080
Forwarding from [::1]:8282 -> 8080
Handling connection for 8282
Handling connection for 8282


# Pour vérifier si keycloak est accessible depuis un autre pod du meme cluster: 
/ # nc -zv my-keycloak.default.svc.cluster.local 80
my-keycloak.default.svc.cluster.local (10.109.69.223:80) open


# install curl sur un pod du cluster 
apk add --no-cache curl

curl  http://my-keycloak.default.svc.cluster.local:80/realms/adjoda-dev-realm
m
{"realm":"adjoda-dev-realm","public_key":"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqO+oMrDPy7pkM8B7blunEdAf4deJE/lS1/iAOEexoMiaNdbc1ydGqxByv7to6k/j3tCpaezgvy9ERTJIZT/zUouSo7RVpHTWAlZNGcTv2AmxMrRI9Axbn7Bdy1KEZHF80BmksauWQn/jeli4Fgwovm0AlehSI9cppTPNrHTv/ug6eIUGRZAGuiE64XnETm56lW4zWBEd8eDtc43OxsM0b+fU0A3Zdcjd9YQrtMDAo3kxsITV6PlmREOydiEWVPx2anr+3Ei5TMYi37wf150LQu5UqAJqHPzPqCtDDAyULa0AM9A/y+SSqPM+oEkJb6Bcoe4iVn/2D9B+TDmAZnmJjwIDAQAB","token-service":"http://my-keycloak.default.svc.cluster.local/realms/adjoda-dev-realm/protocol/openid-connect","account-service":"http://my-keycloak.default.svc.cluster.local/realms/adjoda-dev-realm/account","tokens-not-before":0}/ #



# my-keycloak-second to continue
helm upgrade my-keycloak-second oci://registry-1.docker.io/bitnamicharts/keycloak -f values.yaml

        Keycloak can be accessed through the following DNS name from within your cluster:

    my-keycloak-second.default.svc.cluster.local (port 80)

    To access Keycloak from outside the cluster execute the following commands:
    
    1. Get the Keycloak URL and associate its hostname to your cluster external IP:
    
       export CLUSTER_IP=$(minikube ip) # On Minikube. Use: `kubectl cluster-info` on others K8s clusters
       echo "Keycloak URL: http://keycloak.localhost/"
       echo "$CLUSTER_IP  keycloak.localhost" | sudo tee -a /etc/hosts
    
       2. Access Keycloak using the obtained URL.
       3. Access the Administration Console using the following credentials:
    
    echo Username: admin
    echo Password: $(kubectl get secret --namespace default my-keycloak-second -o jsonpath="{.data.admin-password}" | base64 -d)



# ok fonctionne: keycloak en https
mkdir certs
openssl req -x509 -newkey rsa:2048 -keyout certs/key.pem -out certs/cert.pem -days 365 -nodes -subj "/CN=localhost"


docker run -p 8443:8443 \
-e KEYCLOAK_ADMIN=admin \
-e KEYCLOAK_ADMIN_PASSWORD=admin \
-v "$(pwd)/cert.pem:/opt/keycloak/cert.pem:ro" \
-v "$(pwd)/key.pem:/opt/keycloak/key.pem:ro" \
quay.io/keycloak/keycloak:23.0.6 start-dev \
--https-certificate-file=/opt/keycloak/cert.pem \
--https-certificate-key-file=/opt/keycloak/key.pem \
--https-port=8443


ngrok http https://localhost:8443 --url=keycloak.adjoda.com.ngrok.app