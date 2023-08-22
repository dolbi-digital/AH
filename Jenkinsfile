pipeline {
    agent any

    tools {
        maven 'maven3'
        dockerTool 'docker3'
    }

    stages {
        stage("Get code") {
            steps {
                git 'https://github.com/dolbi-digital/AH.git'
            }
        }
        stage("Run Tests") {
            steps {
                sh "mvn clean test"
            }
        }
    }

    post {
        success {
            allure([
            includeProperties: false,
            jdk: '',
            properties: [],
            reportBuildPolicy: 'ALWAYS',
            results: [[path: 'target/allure-results']]])
        }
    }
}