FROM openjdk:jre

COPY ./target/ticket-seller-0.0.1-SNAPSHOT.jar /usr/src/demo/

WORKDIR /usr/src/demo

EXPOSE 8080

CMD ["java", "-jar", "ticket-seller-0.0.1-SNAPSHOT.jar"]