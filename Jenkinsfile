pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building ...'
                slackSend(color: "#f2bc29", message: "[agaia-files]: Building `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")

                sh './gradlew build'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing ...'
                slackSend(color: "#f2bc29", message: "[agaia-files]: Testing `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")

                sh './gradlew test'
            }
        }
        stage('Docker') {
            steps {
                echo 'Docker ...'
                slackSend(color: "#f2bc29", message: "[agaia-files]: Docker `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")

                sh './gradlew docker'
            }
        }
        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing ....'
                slackSend(color: "#f2bc29", message: "[agaia-files]: Push to docker hub `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")

                sh 'echo "kokakola159753" | docker login -u lubomirhadziev --password-stdin'
                sh "docker push lubomirhadziev/agaia-files:\$(./gradlew properties -q | grep 'version:' | awk '{print \$2}')"
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying ....'
                slackSend(color: "#f2bc29", message: "[agaia-files]: Deploying ... `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")

                sh 'kubectl --kubeconfig=deploy/kube-config.yaml -n agaia apply -f deploy/app.yaml'
            }
        }
    }
}