FROM openjdk:21
ADD target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar
EXPOSE 9102
ENTRYPOINT [ "java", "-jar","eureka-server.jar" ]
