pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                git branch: 'main', url: 'https://github.com/Vladislav3421730/OnlineStoreMicroservicesBackend.git'

                dir('market') {
                    bat 'mvn clean package -DskipTests'
                }
                dir('admin') {
                    bat 'mvn clean package -DskipTests'
                }
                dir('image') {
                    bat './gradlew clean build -x test'
                }
            }
        }
        stage('Test') {
            steps {
                dir('market') {
                    bat 'mvn test'
                }
                dir('image') {
                    bat './gradlew test'
                }
            }
            post {
                always {
                    junit 'market/target/surefire-reports/*.xml'
                    junit 'image/build/test-results/test/*.xml'
                }
            }
        }
        stage ('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-cred-panasik', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                }

                dir('market') {
                    bat 'docker build -t vladislavpanasik/market:latest .'
                    bat 'docker push vladislavpanasik/market:latest'
                }

                dir('image') {
                    bat 'docker build -t vladislavpanasik/image:latest .'
                    bat 'docker push vladislavpanasik/image:latest'
                }

                dir('admin') {
                    bat 'docker build -t vladislavpanasik/admin:latest .'
                    bat 'docker push vladislavpanasik/admin:latest'
                }
            }
        }
    }
}