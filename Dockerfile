# ============================================================
# Dockerfile para la aplicacion "gestion-futbol"
# Construye el proyecto con Maven y lo corre en Tomcat 9
# ============================================================

# ---------- ETAPA 1: Construir el proyecto con Maven ----------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos primero el pom.xml para que Maven descargue las dependencias
# (esto aprovecha la cache de Docker: si el pom.xml no cambia, no vuelve a descargar todo)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el resto del codigo fuente
COPY src ./src

# Compilamos y generamos el archivo .war
RUN mvn clean package -DskipTests


# ---------- ETAPA 2: Correr el .war en Tomcat ----------
FROM tomcat:9.0-jdk21

# Borramos las aplicaciones de ejemplo que trae Tomcat por defecto
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos el .war generado en la etapa anterior, con el nombre ROOT.war
# (ROOT.war hace que la app se sirva en la raiz, ej: http://localhost:8080/
#  en vez de http://localhost:8080/gestion-futbol/)
COPY --from=build /app/target/gestion-futbol.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
