pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'echo "Test"'
                sh 'echo "------"'
                sh 'ls -larth'
                sh 'echo "------"'
                sh '''
                    cd template && 
                    sbt ++$TRAVIS_SCALA_VERSION clean coverage test && 
                    sbt ++$TRAVIS_SCALA_VERSION coverageReport && 
                    sbt ++$TRAVIS_SCALA_VERSION coverageAggregate
                '''
            }
        }
    }
}
