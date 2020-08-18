pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'echo "Test"'
                sh 'echo "------"'
                sh 'ls -larth'
                sh 'echo "------"'
                sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/usr/local/bin/sbt compile"
                sh '''
                    cd template && 
                    "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/usr/local/bin/sbt test" && 
                    "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/usr/local/bin/sbt coverageReport" && 
                    "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/usr/local/bin/sbt coverageAggregate"
                '''
            }
        }
    }
}
