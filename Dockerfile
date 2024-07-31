# Utiliser une image de base Maven pour construire l'application
FROM maven:3.8.5-openjdk-11 AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le reste des fichiers de l'application
COPY src ./src

# Exposer le port 8080
EXPOSE 8080

# Commande pour démarrer l'application
CMD ["mvn","spring-boot:run"]
