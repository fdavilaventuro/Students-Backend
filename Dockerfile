# Etapa 1: build con Maven
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar archivos de Maven (para aprovechar la caché de dependencias)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el resto del código fuente y construir el JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: runtime con JRE liviano
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar el JAR compilado desde la etapa anterior
COPY --from=builder /app/target/students-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
