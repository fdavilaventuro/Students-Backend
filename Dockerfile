FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY target/microservicio-estudiantes-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]