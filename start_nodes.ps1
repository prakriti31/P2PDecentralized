cd target

java -jar p2pdecentralized-1.0-SNAPSHOT.jar --server.port=8080
java -jar p2pdecentralized-1.0-SNAPSHOT.jar --server.port=8081
java -jar p2pdecentralized-1.0-SNAPSHOT.jar --server.port=8082
java -jar p2pdecentralized-1.0-SNAPSHOT.jar --server.port=8083
java -jar p2pdecentralized-1.0-SNAPSHOT.jar --server.port=8084
java -jar p2pdecentralized-1.0-SNAPSHOT.jar --server.port=8085
java -jar p2pdecentralized-1.0-SNAPSHOT.jar --server.port=8086
java -jar p2pdecentralized-1.0-SNAPSHOT.jar --server.port=8087

mvn clean install -DskipTests=true

