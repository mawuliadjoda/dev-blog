# 

https://artifacthub.io/packages/helm/bitnami/postgresql?modal=install

helm install my-postgresql bitnami/postgresql --version 18.1.1 \
--set auth.username=root \
--set auth.password=password \
--set auth.database=mydatabase



# 
host à utiliser pour se connecter plustard depuis un pgadmin dans le même cluster: my-postgresql.default.svc.cluster.local