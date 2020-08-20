pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo "Compiling..."
        sh "${tool name: 'sbt', type:'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt compile"
      }
    }
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
