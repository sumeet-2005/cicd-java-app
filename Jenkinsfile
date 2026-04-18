pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'sumeet-2005/cicd-java-app'
        DOCKER_TAG = "${BUILD_NUMBER}"
        SONAR_PROJECT_KEY = 'sumeet-2005_cicd-java-app'
        SONAR_ORGANIZATION = 'sumeet-2005'
    }
    
    stages {
        
        stage('1. Checkout Code') {
            steps {
                echo '📦 Checking out code from GitHub...'
                git branch: 'main',
                    credentialsId: 'github-token',
                    url: 'https://github.com/sumeet-2005/cicd-java-app.git'
                echo '✅ Code checkout completed'
            }
        }
        
        stage('2. Build Application') {
            steps {
                echo '🔨 Building application with Maven...'
                sh 'mvn clean compile'
                echo '✅ Build completed'
            }
        }
        
        stage('3. Run Unit Tests') {
            steps {
                echo '🧪 Running unit tests...'
                sh 'mvn test'
                echo '✅ Tests passed'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('4. SonarQube Analysis') {
            steps {
                echo '🔍 Running SonarQube analysis...'
                withSonarQubeEnv('SonarQubeCloud') {
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.organization=${SONAR_ORGANIZATION} \
                        -Dsonar.host.url=https://sonarcloud.io
                    '''
                }
                echo '✅ SonarQube analysis completed'
            }
        }
        
        stage('5. Build Docker Image') {
            steps {
                echo '🐳 Building Docker image...'
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
                echo '✅ Docker image built'
            }
        }
        
        stage('6. Push Docker Image') {
            steps {
                echo '📤 Pushing Docker image to Docker Hub...'
                script {
                    docker.withRegistry('', 'docker-hub-credentials') {
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push('latest')
                    }
                }
                echo '✅ Docker image pushed'
            }
        }
        
        stage('7. Deploy Application') {
            steps {
                echo '🚀 Deploying application...'
                sh '''
                    docker stop cicd-app || true
                    docker rm cicd-app || true
                    docker run -d --name cicd-app -p 8081:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}
                '''
                echo '✅ Deployment completed'
            }
        }
        
        stage('8. Notify Success') {
            steps {
                echo '🎉 Pipeline completed successfully!'
                echo "Docker Image: ${DOCKER_IMAGE}:${DOCKER_TAG}"
                echo "SonarQube Report: https://sonarcloud.io/project/overview?id=${SONAR_PROJECT_KEY}"
            }
        }
    }
    
    post {
        failure {
            echo '❌ Pipeline failed! Check the logs above.'
        }
        always {
            echo '🏁 Pipeline execution finished'
        }
    }
}
