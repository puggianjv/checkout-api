#!groovy
pipeline {

    agent {
        label 'slave1'
    }

    tools {
        maven 'Maven'
        jdk 'JDK 8u152'
    }

    stages {
      stage('Build') {
        steps {
          echo "Notificar início de build"
          notifySlackVA ("Estou iniciando o processo de build do Completo", "INICIADO")

          echo "Executando build do projeto"
          sh 'mvn clean install'
        }
      }

	    stage('Analyse') {
        steps {
	        withSonarQubeEnv('sonar.fiap.com.br') {
            sh 'mvn sonar:sonar'
	        }
        }
	    }

      stage ('Deploy to Develop') {
        when {
          branch 'developer'
        }
        steps {
          echo '#########################'
          echo 'Preparing workspace'
          echo '#########################'
          sh """
            rm -rf ../fiap-deploy-dev
            mkdir ../fiap-deploy-dev
            mkdir ../fiap-deploy-dev/target
            cp -r Dockerfile ../fiap-deploy-dev/Dockerfile
            cp -r target/*.war ../fiap-deploy-dev/target/
            cp -r .ebextensions ../fiap-deploy-dev/.ebextensions
            cp -r .configuration/context.xml ../fiap-deploy-dev/target/context.xml
            cp -r .elasticbeanstalk ../fiap-deploy-dev/.elasticbeanstalk
            cp -r .configuration/newrelic/ ../fiap-deploy-dev/newrelic
            cp -r .configuration/certs/ ../fiap-deploy-dev/certs
          """
          echo '##################################'
          echo 'Deploying to dev environment'
          echo '##################################'
          withAWS(credentials:'svc_elasticbeanstalk_app') {
            sh """
              cd ../fiap-deploy-dev
              eb deploy fiap-dev
            """
            }
          }
        }

        stage ('Deploy to Homolog') {
          when {
            branch 'homolog'
          }
          steps {
            echo '#########################'
            echo 'Preparing workspace'
            echo '#########################'
            sh """
              rm -rf ../fiap-deploy-hom
              mkdir ../fiap-deploy-hom
              mkdir ../fiap-deploy-hom/target
              cp -r Dockerfile ../fiap-deploy-hom/Dockerfile
              cp -r target/*.war ../fiap-deploy-hom/target/
              cp -r .ebextensions ../fiap-deploy-hom/.ebextensions
              cp -r .configuration/context.xml ../fiap-deploy-hom/target/context.xml
              cp -r .elasticbeanstalk ../fiap-deploy-hom/.elasticbeanstalk
              cp -r .configuration/newrelic/ ../fiap-deploy-hom/newrelic
              cp -r .configuration/certs/ ../fiap-deploy-hom/certs
            """
            echo '##################################'
            echo 'Deploying to dev environment'
            echo '##################################'
            withAWS(credentials:'svc_elasticbeanstalk_app') {
              sh """
                cd ../fiap-deploy-hom
                eb deploy fiap-prd
              """
              }
            }
          }
    }

    post {

        success {
            notifySlackVA ("Tudo certo com o seu build", "SUCESSO")
        }

        failure {
            notifySlackVA ("Algo aconteceu de errado neste build", "ERRO")
        }
    }
}

def notifySlackVA(String details, String buildStatus = 'INICIADO') {

    def url = "https://fiap-slack.com/service/hooks/jenkins-ci/"
    def token = "puDAFSml6ciHD6qL3x598MZu"
    def channel = "#completo"

    buildStatus =  buildStatus ?: 'SUCESSO'

    def subject = "${buildStatus}: ${details}. Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
    def summary = "${subject} (${env.BUILD_URL})"

    def colorCode
    if (buildStatus == 'INICIADO') {
        colorCode = '#FFFF00' // Yellow
    } else if (buildStatus == 'SUCESSO') {
        colorCode = '#00FF00' // Green
    } else {
        colorCode = '#FF0000' //Red
    }

    slackSend (
        baseUrl: url,
        token: token,
        channel: channel,
        color: colorCode,
        message: summary
    )
}
