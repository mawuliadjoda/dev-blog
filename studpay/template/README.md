./mvnw clean package -DskipTests

docker build -t adjodamawuli/template:1.0.0 .

docker run -p 8080:8080 adjodamawuli/template:1.0.0


curl -X POST http://localhost:8080/api/v1/products \
-H "Content-Type: application/json" \
-d '{
        "name": "test2",
        "price": 10,
        "quantity": 10
    }'

curl http://localhost:8080/api/v1/products


docker push adjodamawuli/template:1.0.0