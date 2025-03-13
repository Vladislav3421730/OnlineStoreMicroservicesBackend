pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Vladislav3421730/OnlineStoreMicroservicesFrontend.git'
            }
        }
        stage ('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-cred-panasik', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                }
                bat 'docker build -t vladislavpanasik/frontend:latest .'
                bat 'docker push vladislavpanasik/frontend:latest'
            }
        }
    }
}