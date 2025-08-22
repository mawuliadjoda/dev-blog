# keycloak en https
mkdir certs
openssl req -x509 -newkey rsa:2048 -keyout certs/key.pem -out certs/cert.pem -days 365 -nodes -subj "/CN=localhost"

cd certs/
docker run -p 8443:8443 \
-e KEYCLOAK_ADMIN=admin \
-e KEYCLOAK_ADMIN_PASSWORD=admin \
-v "$(pwd)/cert.pem:/opt/keycloak/cert.pem:ro" \
-v "$(pwd)/key.pem:/opt/keycloak/key.pem:ro" \
quay.io/keycloak/keycloak:23.0.6 start-dev \
--https-certificate-file=/opt/keycloak/cert.pem \
--https-certificate-key-file=/opt/keycloak/key.pem \
--https-port=8443


# ngrok
https://dashboard.ngrok.com/domains
ngrok http https://localhost:8443 --url=keycloak.adjoda.com.ngrok.app




# 
lefort@GT13Pro:/mnt/d/dev/git/dev-blog/kubernetes/spring-boot-helm/deploy-keycloak/certs$ docker run -p 8443:8443 \
-e KEYCLOAK_ADMIN=admin \
-e KEYCLOAK_ADMIN_PASSWORD=admin \
-v "$(pwd)/cert.pem:/opt/keycloak/cert.pem:ro" \
-v "$(pwd)/key.pem:/opt/keycloak/key.pem:ro" \
quay.io/keycloak/keycloak:23.0.6 start-dev \
--https-certificate-file=/opt/keycloak/cert.pem \
--https-certificate-key-file=/opt/keycloak/key.pem \
--https-port=8443
Updating the configuration and installing your custom providers, if any. Please wait.
2025-08-05 21:46:37,314 INFO  [io.quarkus.deployment.QuarkusAugmentor] (main) Quarkus augmentation completed in 11614ms