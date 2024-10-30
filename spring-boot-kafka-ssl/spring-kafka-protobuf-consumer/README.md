API REST du Schema Registry

http://localhost:8081/subjects/person-topic-value/versions/latest



Utiliser Control Center

http://localhost:9021

schema depuis le control cender
http://localhost:9021/clusters/R0cQ2Wq1ScyKmgWPQyetTA/management/topics/person-topic/schema/value



https://protobuf.dev/

`message Person {
optional string name = 1;
optional int32 id = 2;
optional string email = 3;
}
Figure 1. A proto definition.`


// Java code
        `Person john = Person.newBuilder()
        .setId(1234)
        .setName("John Doe")
        .setEmail("jdoe@example.com")
        .build();
        output = new FileOutputStream(args[0]);
        john.writeTo(output);`

@see https://medium.com/@pravin3c/spring-kafka-using-protobuf-as-a-message-format-6370e44bcf21

@see https://blog.devgenius.io/json-vs-protobuf-and-using-protobuf-with-kafka-e412b159a5ca


@see Error handling : https://medium.com/@pravin3c/poison-pill-in-kafka-and-ways-to-handle-it-157fbc09338b