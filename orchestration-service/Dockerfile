FROM openjdk:jre

COPY ./target/orchestration-service-0.0.1-SNAPSHOT.jar /usr/src/demo/

WORKDIR /usr/src/demo

### FOR DEBUG 
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
EXPOSE 5005
#######

EXPOSE 8080

CMD ["java", "-jar", "orchestration-service-0.0.1-SNAPSHOT.jar"]