#Base Image
FROM openjdk:17-jdk-alpine
#Copy file.jar ke dalam image
ADD target/shopify-0.0.1-SNAPSHOT.jar shopify-0.0.1-SNAPSHOT.jar
#Command untuk menjalankan file.jar
CMD["java", "-jar", "shopify-0.0.1-SNAPSHOT.jar"]