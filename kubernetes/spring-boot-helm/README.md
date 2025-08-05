./mvnw clean package -DskipTests
docker build -t adjodamawuli/spring-boot-helm:1.0.0 .
docker run -p 8080:8080 adjodamawuli/spring-boot-helm:1.0.0
docker push adjodamawuli/spring-boot-helm:1.0.0



minikube start
minikube dashboard
minikube tunnel


https://dashboard.ngrok.com/domains
https://dashboard.ngrok.com/get-started/your-authtoken
ngrok config add-authtoken $YOUR_AUTHTOKEN

ngrok http https://localhost:8443 --url=keycloak.adjoda.com.ngrok.app