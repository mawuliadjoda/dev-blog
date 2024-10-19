https://docs.confluent.io/platform/current/get-started/platform-quickstart.html


cd /mnt/d/dev/Java/git/Kafka-Streams-with-Spring-Cloud/kafka-installation


docker compose up -d
docker compose ps
	lefort@ADJODA:/mnt/d/dev/Java/git/Kafka-Streams-with-Spring-Cloud/kafka-installation$ docker compose ps
	WARN[0000] /mnt/d/dev/Java/git/Kafka-Streams-with-Spring-Cloud/kafka-installation/docker-compose.yml: the attribute `version` is obsolete, it will be ignored, please remove it to avoid potential confusion
	NAME              IMAGE                                             COMMAND                  SERVICE           CREATED          STATUS          PORTS
	broker            confluentinc/cp-kafka:7.7.0                       "/etc/confluent/dock…"   broker            10 minutes ago   Up 10 minutes   0.0.0.0:9092->9092/tcp, :::9092->9092/tcp, 0.0.0.0:9101->9101/tcp, :::9101->9101/tcp
	connect           cnfldemos/cp-server-connect-datagen:0.6.4-7.6.0   "/etc/confluent/dock…"   connect           10 minutes ago   Up 10 minutes   0.0.0.0:8083->8083/tcp, :::8083->8083/tcp, 9092/tcp
	control-center    confluentinc/cp-enterprise-control-center:7.7.0   "/etc/confluent/dock…"   control-center    10 minutes ago   Up 10 minutes   0.0.0.0:9021->9021/tcp, :::9021->9021/tcp
	ksql-datagen      confluentinc/ksqldb-examples:7.7.0                "bash -c 'echo Waiti…"   ksql-datagen      10 minutes ago   Up 10 minutes
	ksqldb-cli        confluentinc/cp-ksqldb-cli:7.7.0                  "/bin/sh"                ksqldb-cli        10 minutes ago   Up 10 minutes
	ksqldb-server     confluentinc/cp-ksqldb-server:7.7.0               "/etc/confluent/dock…"   ksqldb-server     10 minutes ago   Up 10 minutes   0.0.0.0:8088->8088/tcp, :::8088->8088/tcp
	rest-proxy        confluentinc/cp-kafka-rest:7.7.0                  "/etc/confluent/dock…"   rest-proxy        10 minutes ago   Up 10 minutes   0.0.0.0:8082->8082/tcp, :::8082->8082/tcp
	schema-registry   confluentinc/cp-schema-registry:7.7.0             "/etc/confluent/dock…"   schema-registry   10 minutes ago   Up 10 minutes   0.0.0.0:8081->8081/tcp, :::8081->8081/tcp

docker exec -it broker /bin/sh
	lefort@ADJODA:/mnt/d/dev/Java/git/Kafka-Streams-with-Spring-Cloud/kafka-installation$ docker exec -it broker /bin/sh
	sh-4.4$ ls
	sh-4.4$ ls
	sh-4.4$ kafka-console-producer --topic test_topic --broker-list localhost:9092
	>{"name": "ADJODA Mawuli", "age": 34, "genre": "male"}
	>{"name": "ADJODA Anaya", "age":1, "genre": "female"}

# In another terminal
lefort@ADJODA:~$ cd /mnt/d/dev/Java/git/Kafka-Streams-with-Spring-Cloud/kafka-installation
lefort@ADJODA:/mnt/d/dev/Java/git/Kafka-Streams-with-Spring-Cloud/kafka-installation$ docker exec -it broker /bin/sh

sh-4.4$ kafka-console-consumer --topic test_topic --bootstrap-server localhost:9092 --from-beginning
{"name": "ADJODA Mawuli", ����"age": 34, "genre": "male"}
{"name": "ADJODA Anaya", "age":1, "genre": "female"}



API REST du Schema Registry

http://localhost:8081/subjects/person-topic-value/versions/latest



Utiliser Control Center

http://localhost:9021

schema depuis le control cender
http://localhost:9021/clusters/R0cQ2Wq1ScyKmgWPQyetTA/management/topics/person-topic/schema/value