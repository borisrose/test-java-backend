# Utiliser une image de base Maven pour construire l'application
FROM maven:3.8.5-openjdk-11 AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le reste des fichiers de l'application
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Utiliser une image Java légère pour exécuter l'application
FROM openjdk:11-jre-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le jar construit depuis l'étape précédente
COPY --from=build /app/target/*.jar /app/app.jar

# Exposer le port 8080
EXPOSE 8080

# Commande pour démarrer l'application
CMD ["java", "-jar", "/app/app.jar"]
