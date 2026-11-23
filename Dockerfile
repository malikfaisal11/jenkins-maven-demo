FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/jenkins-maven-demo-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "--add-modules=jdk.httpserver", "-cp", "app.jar", "com.gobeyond.registration.App"]