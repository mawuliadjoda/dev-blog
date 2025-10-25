helm repo add runix https://helm.runix.net/
helm repo update

helm install pgadmin runix/pgadmin4 -f pgadmin-values.yaml

# minikube addons enable ingress ==> non on utilise traefik
# helm repo add traefik https://traefik.github.io/charts
# helm repo update
# 
# helm install traefik traefik/traefik \
# --namespace kube-system



minikube tunnel

# /etc/hosts : C:\Windows\System32\drivers\etc
127.0.0.1 pgadmin.localhost


# pour se connecter à postgresql déployer dans le cluster:
kubectl get services pour voir le nom du service:

Host: my-postgresql.default.svc.cluster.local
user: root
password: password
database: mydatabase


@see folder /deploy-postgresql


# dashboard 
http://pgadmin.localhost/