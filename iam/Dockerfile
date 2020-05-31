FROM openjdk:jre

COPY ./target/iam-0.0.1-SNAPSHOT.jar /usr/src/demo/

WORKDIR /usr/src/demo

EXPOSE 8080

CMD ["java", "-jar", "iam-0.0.1-SNAPSHOT.jar"]