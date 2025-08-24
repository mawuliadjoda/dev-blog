./mvnw clean package -DskipTests
docker build -t adjodamawuli/product:1.0.0 .
docker run -p 8080:8080 adjodamawuli/product:1.0.0
docker push adjodamawuli/product:1.0.0


@see https://github.com/mawuliadjoda/dev-blog/tree/develop/spring-data-rest-projection
https://github.com/mawuliadjoda/dev-blog/blob/pagination/spring-data-jpa-pagination/spring-data-jpa-pagination-tdd/src/main/java/com/esprit/pagination/infrastructure/adapter/output/persistence/mapper/GenericPersistenceMapper.java


@see https://github.com/mawuliadjoda/springboot-best-practices/blob/master/src/test/java/com/adjoda/post/PostControllerTest.java