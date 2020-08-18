pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                echo 'Test'
                sh 'cd template && 
                sbt ++$TRAVIS_SCALA_VERSION clean coverage test && 
                sbt ++$TRAVIS_SCALA_VERSION coverageReport && 
                sbt ++$TRAVIS_SCALA_VERSION coverageAggregate'
            }
        }
    }
}
