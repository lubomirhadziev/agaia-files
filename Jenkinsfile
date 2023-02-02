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
        stage('Publish') {
            steps {
                echo 'Publish in private maven repository ...'
                slackSend(color: "#f2bc29", message: "[agaia-files]: Publish `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")

                sh './gradlew publish'
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
                sh "docker push lubomirhadziev/agaia-files:\$(cat gradle.properties | grep 'VERSION=' | awk -F= '{print \$2}')"
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying ....'
                slackSend(color: "#f2bc29", message: "[agaia-files]: Deploying ... `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}")

                sh 'doctl kubernetes cluster kubeconfig show 08a3d678-c6ee-42e2-b222-a2b8b2876d0d > deploy/kube-config.yaml'
                sh 'kubectl --kubeconfig=deploy/kube-config.yaml -n agaia apply -f deploy/app.yaml'
            }
        }
    }
}