# using database ==> to deploy first

helm install my-postgres bitnami/postgresql \
--set auth.username=root \
--set auth.password=password \
--set auth.database=mydatabase