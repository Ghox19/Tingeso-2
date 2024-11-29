FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} loanEntity-microservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/loanEntity-microservice-0.0.1-SNAPSHOT.jar"]