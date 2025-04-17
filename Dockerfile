FROM eclipse-temurin:21-jdk
VOLUME /tmp
COPY application/build/libs/app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]