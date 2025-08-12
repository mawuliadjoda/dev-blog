./mvnw clean package -DskipTests
docker build -t adjodamawuli/ecommerce/product:1.0.0 .
docker run -p 8080:8080 adjodamawuli/ecommerce/product:1.0.0
docker push adjodamawuli/ecommerce/product:1.0.0