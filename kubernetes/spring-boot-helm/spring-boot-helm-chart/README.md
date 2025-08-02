helm install spring-boot-helm ./spring-boot-helm-chart/
helm uninstall spring-boot-helm

# using database 

helm install my-postgres bitnami/postgresql \
--set auth.username=root \
--set auth.password=password \
--set auth.database=mydatabase

# pour rendre accessible depuis l'exterieur
kubectl port-forward svc/my-postgres-postgresql 5432:5432 