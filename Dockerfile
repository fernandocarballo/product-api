FROM openjdk:21-jdk-alpine
EXPOSE 8070
ADD target/product-api.jar
ENTRYPOINT ["java","-jar","/product-api.jar"]