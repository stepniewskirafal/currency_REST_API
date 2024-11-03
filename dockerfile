FROM amazoncorretto:17-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/CurrencyExchange-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
