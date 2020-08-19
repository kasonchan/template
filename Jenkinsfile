pipeline {
    agent any
    environment {
      SBT_HOME = tool name: 'sbt.1.3.8', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'
      PATH = "${env.SBT_HOME}/bin:${env.PATH}"
    }
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
            }
        }
    }
}
