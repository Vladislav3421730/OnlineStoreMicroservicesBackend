FROM amazoncorretto:17
ARG JAR_FILE=deploy/*.jar
COPY ${JAR_FILE} application.jar
CMD apt-get update -y
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/application.jar"]