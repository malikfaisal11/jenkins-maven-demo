FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/jenkins-maven-demo-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-cp", "app.jar", "com.gobeyond.registration.App"]