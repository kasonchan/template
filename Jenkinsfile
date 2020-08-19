pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'echo "Test"'
                sh 'echo "------"'
                sh 'ls -larth'
                sh 'echo "------"'
                sh 'cd template' 
                sh "sbt test"
                sh "sbt coverageReport" 
                sh "sbt coverageAggregate"
            }
        }
    }
}
