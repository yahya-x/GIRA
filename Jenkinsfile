pipeline {
    agent any


    triggers {
        githubPush()
    }

    environment {
        DOCKER_COMPOSE_DEV = "docker/docker-compose.dev.yaml"
        DOCKER_COMPOSE_TEST = "docker/docker-compose.test.yaml"
        DOCKER_COMPOSE_MONITORING = "docker/docker-compose.monitoring.yaml"
        ENV_FILE_DEV = "docker/.env.dev"
        ENV_FILE_TEST = "docker/.env.test"
        BACKEND_IMAGE = "gira-backend:ci"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Lint & Test Backend') {
            steps {
                dir('backend') {
                    sh './mvnw clean verify'
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                sh 'docker build -t $BACKEND_IMAGE -f docker/Dockerfile.backend backend'
            }
        }

        stage('Test Docker Compose Stack') {
            steps {
                sh 'docker compose -f $DOCKER_COMPOSE_TEST --env-file $ENV_FILE_TEST up --build --abort-on-container-exit'
            }
            post {
                always {
                    sh 'docker compose -f $DOCKER_COMPOSE_TEST down -v'
                }
            }
        }

        stage('Integration Monitoring') {
            steps {
                sh 'docker compose -f $DOCKER_COMPOSE_DEV -f $DOCKER_COMPOSE_MONITORING --env-file $ENV_FILE_DEV up -d --build'
                sh 'sleep 30' // Laisse le temps aux services de démarrer
                sh 'docker compose -f $DOCKER_COMPOSE_DEV -f $DOCKER_COMPOSE_MONITORING logs'
            }
            post {
                always {
                    sh 'docker compose -f $DOCKER_COMPOSE_DEV -f $DOCKER_COMPOSE_MONITORING down -v'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'backend/target/*.jar', allowEmptyArchive: true
            }
        }

        // Optionnel : push image sur un registry
        // stage('Push Docker Image') {
        //     steps {
        //         withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        //             sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
        //             sh 'docker tag $BACKEND_IMAGE yourrepo/gira-backend:${BUILD_NUMBER}'
        //             sh 'docker push yourrepo/gira-backend:${BUILD_NUMBER}'
        //         }
        //     }
        // }
    }
}