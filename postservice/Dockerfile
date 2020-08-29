FROM openjdk:jre

COPY ./target/postservice-0.0.1-SNAPSHOT.jar /usr/src/demo/

WORKDIR /usr/src/demo

#### FOR DEBUG 
# ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006
# EXPOSE 5006
########

EXPOSE 8080

CMD ["java", "-jar", "postservice-0.0.1-SNAPSHOT.jar"]