pipeline {
    agent {
        label 'Jenkins-Agent'
    }

    options {
        skipDefaultCheckout(true)
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code from GitHub'
                checkout scm
            }
        }

        stage('Check Environment') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
                sh 'git --version'
                sh 'docker --version'
            }
        }

        stage('Build and Test') {
            steps {
                echo 'Running Maven tests'
                sh 'mvn clean test'
            }
        }

        stage('Package Application') {
            steps {
                echo 'Packaging Java application'
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image'
                sh 'docker build -t jenkins-maven-demo:v1 .'
            }
        }

        stage('Docker Run') {
            steps {
                echo 'Testing local Docker image'
                sh 'docker run --rm jenkins-maven-demo:v1'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKERHUB_USERNAME',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    sh '''
                        set +x
                        trap 'docker logout >/dev/null 2>&1 || true' EXIT

                        echo "$DOCKERHUB_TOKEN" | docker login \
                            --username "$DOCKERHUB_USERNAME" \
                            --password-stdin

                        docker tag jenkins-maven-demo:v1 \
                            "$DOCKERHUB_USERNAME/jenkins-maven-demo:${BUILD_NUMBER}"

                        docker tag jenkins-maven-demo:v1 \
                            "$DOCKERHUB_USERNAME/jenkins-maven-demo:latest"

                        docker push "$DOCKERHUB_USERNAME/jenkins-maven-demo:${BUILD_NUMBER}"

                        docker push "$DOCKERHUB_USERNAME/jenkins-maven-demo:latest"
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Testing deployment from Docker Hub'

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKERHUB_USERNAME',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    sh '''
                        set +x
                        trap 'docker logout >/dev/null 2>&1 || true' EXIT

                        echo "$DOCKERHUB_TOKEN" | docker login \
                            --username "$DOCKERHUB_USERNAME" \
                            --password-stdin

                        docker pull "$DOCKERHUB_USERNAME/jenkins-maven-demo:${BUILD_NUMBER}"

                        docker run --rm \
                            "$DOCKERHUB_USERNAME/jenkins-maven-demo:${BUILD_NUMBER}"
                    '''
                }
            }
        }

        stage('Verify JAR') {
            steps {
                echo 'Verifying packaged JAR'
                sh 'ls -lh target/'
            }
        }
    }

    post {
        success {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            echo 'CI/CD Pipeline completed successfully!'
        }

        failure {
            echo 'CI/CD Pipeline failed. Check the console output.'
        }
    }
}