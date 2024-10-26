keytool -keystore server.keystore.jks -alias localhost -validity 5000 -genkey -keyalg RSA
keytool -list -v -keystore server.keystore.jks
openssl req -new -x509 -keyout ca-key -out ca-cert -days 5000
keytool -keystore client.truststore.jks -alias CARoot -import -file ca-cert
keytool -keystore server.truststore.jks -alias CARoot -import -file ca-cert
keytool -keystore server.keystore.jks -alias localhost -certreq -file cert-file
openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed -days 5000 -CAcreateserial -passin pass:test