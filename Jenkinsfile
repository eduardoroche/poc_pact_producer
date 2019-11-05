#!groovy
pipeline {

  agent any

  environment {
    BRANCH_NAME=env.GIT_BRANCH.replace("origin/", "")
  }

  parameters {
    string(name: 'pactConsumerTags', defaultValue: 'master')
  }

  tools {
	maven 'maven'
  }

  stages {
    stage ('Build') {
      steps {
      // it is possible set prod as tag
      echo "version ${params.pactConsumerTags}"
		sh "mvn clean verify -Dpact.provider.version=${GIT_COMMIT} -Dpactbroker.url=${PACT_BROKER_URL} -Dpactbroker.port=${PACT_BROKER_PORT} -Dpact.verifier.publishResults=true  -Dpactbroker.tags=${params.pactConsumerTags}"
      }
    }
    stage('Check Pact Verifications') {
      steps {
        sh 'curl -LO https://github.com/pact-foundation/pact-ruby-standalone/releases/download/v1.61.1/pact-1.61.1-linux-x86_64.tar.gz'
        sh 'tar xzf pact-1.61.1-linux-x86_64.tar.gz'
        dir('pact/bin') {
            // --to prod -- set it in case you want to deploy the PROD
            // --to prod could be master and it check for consumers that committed to this tag, if it pass, so everything is alright, if it does not, the consumer has not committed so far.
          sh "./pact-broker can-i-deploy -a user-service -b http://pact_broker -e ${GIT_COMMIT} --to prod"
          sh "./pact-broker can-i-deploy -a user-service-messaging -b http://pact_broker -e ${GIT_COMMIT} --to prod"
        }
      }
    }
    stage('Deploy') {
      when {
        branch 'master'
      }
      steps {
        echo 'Deploying to prod now...'
      }
    }
   /* stage('Tag Pact') {
      steps {
        dir('pact/bin') {
          sh "./pact-broker create-version-tag -a user-service -b http://pact_broker -e ${GIT_COMMIT} -t prod"
        }
      }
    }*/
  }

  /**stages {
    stage('Get Latest Prod Version From Pact Broker') {
      steps {
        sh 'curl -LO https://github.com/pact-foundation/pact-ruby-standalone/releases/download/v1.61.1/pact-1.61.1-linux-x86_64.tar.gz'
        sh 'tar xzf pact-1.61.1-linux-x86_64.tar.gz'
        dir('pact/bin') {
          script {
            env.PROD_VERSION = sh(script: "./pact-broker describe-version -a user-service -b http://pact_broker -l prod | tail -1 | cut -f 1 -d \\|", returnStdout: true).trim()
          }
        }
        echo "Current prod version: " + PROD_VERSION
      }
    }
    stage("Checkout Latest Prod Version") {
      steps {
        sh "git checkout ${PROD_VERSION}"
      }
    }

    stage('Run Contract Tests') {
      steps {
        sh "mvn clean test " +
                "-Pcontract-tests " +
                "-Dpact.provider.version=${PROD_VERSION} " +
                "-Dpact.verifier.publishResults=true " +
                "-Dpactbroker.tags=prod,${params.pactConsumerTags}"
      }
    }
  }**/

}