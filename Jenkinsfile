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
                sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt test"
                sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt coverageReport" 
                sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt coverageAggregate"
            }
        }
    }
}
