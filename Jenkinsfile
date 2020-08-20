pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh 'echo "Test"'
        sh 'echo "------"'
        sh 'ls -larth'
        sh 'echo "------"'
        sh 'cd template && ls -larth && sbt test && sbt coverageReport'
      }
    }
  }
}
