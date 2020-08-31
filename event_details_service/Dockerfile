FROM openjdk:jre

COPY ./target/eventdetailsservice-0.0.1-SNAPSHOT.jar /usr/src/demo/

WORKDIR /usr/src/demo

### FOR DEBUG 
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5008
EXPOSE 5008
#######

EXPOSE 8080

CMD ["java", "-jar", "eventdetailsservice-0.0.1-SNAPSHOT.jar"]