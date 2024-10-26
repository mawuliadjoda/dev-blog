# To run this command bellow to generate neccessary certificate for the test

    sh generate_certificate.sh

------------ Kafka
1. Génération du Keystore pour Kafka
   Commande pour Générer le Keystore de Kafka

       keytool -genkeypair -alias kafka-server \
       -keyalg RSA -keysize 2048 \
       -keystore kafka.server.keystore.jks -validity 365 \
       -storepass password \
       -dname "CN=localhost, OU=TD Bank ON, O=TD Bank, L=Toronto, S=ON, C=CA"


   Explications des paramètres :
		-alias kafka-server : nom de l'alias pour le certificat.
		-keyalg RSA : spécifie l'algorithme de clé.
		-keysize 2048 : taille de la clé.
		-keystore kafka.server.keystore.jks : nom du fichier du keystore.
		-validity 365 : validité du certificat en jours.
		-dname : le nom Distinguished Name (DN) qui inclut CN=localhost, ce qui est crucial pour éviter les erreurs de validation de nom d'hôte.

   => 
   KAFKA_SSL_KEYSTORE_CREDENTIALS: Le mot de passe du keystore est défini lors de sa création dans la commande -storepass utilisée ici. 
             
		  keytool -genkeypair -alias kafka-server \
		  -keyalg RSA -keysize 2048 \
		  -keystore kafka.server.keystore.jks -validity 365 \
		  -storepass password \
		  -dname "CN=localhost, OU=TD Bank ON, O=TD Bank, L=Toronto, S=ON, C=CA"
		  
  =>
  KAFKA_SSL_KEY_CREDENTIALS: Par défaut, Keytool utilise le même mot de passe pour le keystore et la clé privée. Cependant, si vous souhaitez définir un mot de passe différent pour la clé privée, vous pouvez le faire en ajoutant l’option -keypass lors de la création de la paire de clés :
         
		 keytool -genkeypair -alias kafka-server \
		  -keyalg RSA -keysize 2048 \
		  -keystore kafka.server.keystore.jks -validity 365 \
		  -storepass password \
		  -keypass privatekeypassword \
		  -dname "CN=localhost, OU=TD Bank ON, O=TD Bank, L=Toronto, S=ON, C=CA"


2. Exporter le Certificat de Kafka
   Une fois que le keystore est généré, exportez le certificat :

       keytool -exportcert -alias kafka-server \
       -keystore kafka.server.keystore.jks -file kafka-server.cert \
       -storepass password


3. Génération du Truststore pour Kafka
   Commande pour Générer le Truststore de Kafka

       keytool -importcert -file kafka-server.cert \
       -keystore kafka.server.truststore.jks -alias kafka-server-cert \
       -storepass password -noprompt

   Explication : Cette commande importe le certificat du serveur Kafka dans le truststore. L'option -noprompt évite une confirmation interactive.





---------client Spring boot

4. Génération du Keystore pour l'Application Spring Boot
   Commande pour Générer le Keystore de l'Application

       keytool -genkeypair -alias spring-client \
       -keyalg RSA -keysize 2048 \
       -keystore client.keystore.jks -validity 365 \
       -storepass password \
       -dname "CN=localhost, OU=Connect228, O=Connect228, L=Lome, S=Maritime, C=TG"


5. Exporter le Certificat de l'Application
   Exportez le certificat du client :

       keytool -exportcert -alias spring-client \
       -keystore client.keystore.jks -file spring-client.cert \
       -storepass password


6. Génération du Truststore pour l'Application Spring Boot
   Commande pour Générer le Truststore de l'Application

       keytool -importcert -file kafka-server.cert \
       -keystore client.truststore.jks -alias kafka-server-cert \
       -storepass password -noprompt


--- Important si on veut two way ssl : KAFKA_SSL_CLIENT_AUTH: 'required'
7. Importer le certificat du client dans le truststore kafka

       keytool -importcert -file spring-client.cert \
       -keystore kafka.server.truststore.jks -alias spring-client \
       -storepass password -noprompt
