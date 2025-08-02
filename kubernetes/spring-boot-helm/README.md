./mvnw clean package -DskipTests
docker build -t adjodamawuli/spring-boot-helm:1.0.0 .
docker run -p 8080:8080 adjodamawuli/spring-boot-helm:1.0.0
docker push adjodamawuli/spring-boot-helm:1.0.0
