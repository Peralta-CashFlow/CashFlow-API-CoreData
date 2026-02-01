pipeline {
    agent any

    tools {
        jdk 'JDK25'
        maven "3.9.10"
    }

    parameters {
        string(name: 'version', description: 'The version of the new image.')
    }

    stages {

        stage('Validate Parameters') {
            steps {
                script {
                    if (!params.version?.trim()) {
                        error "Version is mandatory, please define the value and run the pipeline again."
                    }
                }
            }
        }

        stage('Build JAR') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'github-packages',
                        usernameVariable: 'GITHUB_USERNAME',
                        passwordVariable: 'GITHUB_TOKEN'
                    )
                ]) {
                    configFileProvider([
                        configFile(fileId: 'cash-flow-settings.xml', variable: 'SETTINGS_FILE')
                    ]) {
                        script {
                            if (isUnix()) {
                                sh '''
                                    sed -i 's|_GITHUB_USERNAME_|${GITHUB_USERNAME}|' $SETTINGS_FILE
                                    sed -i 's|_GITHUB_TOKEN_|${GITHUB_TOKEN}|' $SETTINGS_FILE
                                    mvn clean install -s $SETTINGS_FILE
                                '''
                            } else {
                                bat """
                                    powershell -Command "(Get-Content %SETTINGS_FILE%) -replace '_GITHUB_USERNAME_', '%GITHUB_USERNAME%' | Set-Content %SETTINGS_FILE%"
                                    powershell -Command "(Get-Content %SETTINGS_FILE%) -replace '_GITHUB_TOKEN_', '%GITHUB_TOKEN%' | Set-Content %SETTINGS_FILE%"
                                    mvn clean install -s %SETTINGS_FILE%
                                """
                            }
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')
                ]) {
                    script {
                        def imageTag = "${DOCKER_USERNAME}/cashflow-api-core-data:${params.version}"
                        env.IMAGE_TAG = imageTag
                        if (isUnix()) {
                            sh "docker build -t ${imageTag} ."
                        } else {
                            bat "docker build -t ${imageTag} ."
                        }
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    script {
                        if (isUnix()) {
                            sh """
                                echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
                                docker push ${env.IMAGE_TAG}
                                docker logout
                            """
                        } else {
                            bat """
                                echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin
                                docker push ${env.IMAGE_TAG}
                                docker logout
                            """
                        }
                    }
                }
            }
        }
    }
}
