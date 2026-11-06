# jenkins-maven-demo
DevOps CI/CD Practice Project
# Jenkins Maven Demo

## Project Overview

This project demonstrates a basic Java registration application developed to practice DevOps and CI/CD concepts.

## Technologies

- Java 21
- Apache Maven
- JUnit 5
- Git
- GitHub
- Jenkins

## Project Structure

- `src/main/java`: Application source code.
- `src/test/java`: Unit test cases.
- `pom.xml`: Maven project configuration.

## Application Functionality

The application provides basic user registration validation.

It checks:
- User name must not be empty.
- Email address must have a valid basic format.

## Build the Project

Run:

```bash
mvn clean package
```

## Execute Tests

Run:

```bash
mvn test
```

## Run the Application

Run:

```bash
java -cp target/classes com.gobeyond.registration.App
```

Expected output:

```text
Registration successful for Faisal
```

## Project Goal

This application will be used to practice an end-to-end CI/CD workflow using Jenkins, SonarQube, Docker, Kubernetes and ArgoCD.

## CI/CD Integration

This project now uses Jenkins for automated Maven builds,
JUnit testing and artifact generation.