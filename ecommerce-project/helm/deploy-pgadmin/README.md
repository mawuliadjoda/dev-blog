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

# /etc/hosts
127.0.0.1 pgadmin.localhost


# pour se connecter à postgresql déployer dans le cluster:
kubectl get services pour voir le nom du service:

Host: my-postgres-postgresql
user: root
password: password
database: mydatabase


@see
helm install my-postgres bitnami/postgresql \
--set auth.username=root \
--set auth.password=password \
--set auth.database=mydatabase


# dashboard 
http://pgadmin.localhost/