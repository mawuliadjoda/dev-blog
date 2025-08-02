helm install spring-boot-helm ./spring-boot-helm-chart/
helm uninstall spring-boot-helm

# using database ==> to deploy first

helm install my-postgres bitnami/postgresql \
--set auth.username=root \
--set auth.password=password \
--set auth.database=mydatabase

# pour rendre accessible depuis l'exterieur
kubectl port-forward svc/my-postgres-postgresql 5432:5432 => not working because it's run on wsl. will work if run on windows


# SPRING_DATASOURCE_URL: jdbc:postgresql://my-postgres-postgresql.default.svc.cluster.local:5432/mydatabase

| Partie                   | Rôle                              |
| ------------------------ | --------------------------------- |
| `jdbc:postgresql://`     | Type de connexion JDBC            |
| `my-postgres-postgresql` | Nom du service PostgreSQL Helm    |
| `default`                | Namespace dans Kubernetes         |
| `svc.cluster.local`      | Domaine DNS interne de Kubernetes |
| `5432`                   | Port PostgreSQL                   |
| `/mydatabase`            | Nom de la base de données         |


GET http://adjodadev.com/springboothelm/api/v1/users
  
Response:
[
    {
        "id": 1,
        "name": "Alice",
        "lastname": "Johnson"
    },
    {
        "id": 2,
        "name": "Bob",
        "lastname": "Smith"
    }
]