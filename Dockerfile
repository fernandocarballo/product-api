FROM eclipse-temurin:21
EXPOSE 8070
ADD target/product-api.jar /product-api.jar
ENTRYPOINT ["java","-jar","/product-api.jar"]