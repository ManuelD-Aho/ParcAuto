# 1) Base Debian avec OpenJDK 22 et JavaFX via APT
FROM openjdk:22-jdk-slim

WORKDIR /app

# Installer utilitaires + JavaFX
RUN apt-get update \
 && apt-get install -y findutils openjfx \
 && rm -rf /var/lib/apt/lists/*

# Copier driver JDBC et sources
COPY mysql-connector-j-8.0.33.jar /app/
COPY src/ /app/src/

# Compiler toutes les classes Java avec JavaFX + JDBC
RUN find src/main/java -name "*.java" > sources.txt \
 && javac \
      --module-path /usr/share/openjfx/lib \
      --add-modules javafx.controls,javafx.fxml \
      -cp mysql-connector-j-8.0.33.jar \
      @sources.txt

# Point d’entrée : lance l’application JavaFX
CMD ["java", \
     "--module-path", "/usr/share/openjfx/lib", \
     "--add-modules", "javafx.controls,javafx.fxml", \
     "-cp", "mysql-connector-j-8.0.33.jar:src/main/java", \
     "com.miage.test.MainApp"]
