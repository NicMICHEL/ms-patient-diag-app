FROM openjdk:21
ADD target/gateway-0.0.1-SNAPSHOT.jar gateway.jar
EXPOSE 9004
ENTRYPOINT [ "java", "-jar","gateway.jar" ]
