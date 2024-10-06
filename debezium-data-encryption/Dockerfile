FROM debezium/connect:2.7.1.Final
#Docs url https://docs.confluent.io/platform/current/connect/userguide.html#installing-kconnect-plugins [Define a plugin/Install a plugin]
COPY target/dependency-jars/*.jar /kafka/connect/danekerscode-debezium/
EXPOSE 8083