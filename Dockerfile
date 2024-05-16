FROM openjdk:17-oracle
ARG JAR_FILE=build/libs/*.war
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
