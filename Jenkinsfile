node {

    REGISTRY_CREDENTIALS = "registry.wutiarn.ru"
    REGISTRY_URL = "https://registry.wutiarn.ru"
    env.RANCHER_URL = "https://rancher.wutiarn.ru/v1/projects/1a5"
    RANCHER_API_CREDENTIALS = "api.rancher.wutiarn.ru"

    env.RANCHER_STACK_NAME = "homeserver"
    env.RANCHER_SERVICE_NAME = "flibusta-bot"
    env.RANCHER_STACK_ID = "1e2"

    stage("Checkout") {
        checkout scm
    }

    stage("Build image") {
        image = docker.build("homeserver/flibusta-bot:ci-$env.BUILD_NUMBER")
    }

    stage("Push") {
        docker.withRegistry(REGISTRY_URL, REGISTRY_CREDENTIALS) {
            image.push("latest")
        }
    }

    stage("Artifacts") {
        image.inside {
            sh "cp /code/flibusta-bot.jar ."
            archiveArtifacts "flibusta-bot.jar"
            fingerprint "flibusta-bot.jar"
        }
    }

    stage("Deploy") {
        docker.image("wutiarn/rancher-deployer").inside {
            withCredentials([[$class          : 'UsernamePasswordMultiBinding', credentialsId: RANCHER_API_CREDENTIALS,
                              usernameVariable: 'ACCESS_KEY', passwordVariable: 'SECRET_KEY']]) {
                env.RANCHER_ACCESS_KEY = ACCESS_KEY
                env.RANCHER_SECRET_KEY = SECRET_KEY
            }

            sh "/root/upgrade.sh"
        }
    }
}