FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} user-microservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/user-microservice-0.0.1-SNAPSHOT.jar"]