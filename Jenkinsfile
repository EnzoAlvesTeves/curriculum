// Esteira CI/CD local para branches release/*.
//
// Fluxo: push numa branch release/* → polling do Jenkins (1 min) dispara a
// pipeline → build + testes (JaCoCo) → análise SonarQube → Quality Gate
// (bloqueia se reprovar) → deploy: rebuild e sobe os 4 containers.
//
// Pré-requisitos no Jenkins (ver README.md, seção "Esteira CI/CD"):
//   - Servidor SonarQube cadastrado em "Manage Jenkins → System" com o nome
//     usado em withSonarQubeEnv (abaixo: 'SonarQube') e o token 'sonar-token'.
//   - Webhook no SonarQube apontando para http://jenkins:8080/sonarqube-webhook/
//     (necessário para o waitForQualityGate receber o resultado).
//   - Socket do Docker montado (Testcontainers e deploy via docker compose).
//
// Os módulos usam o Maven Wrapper (./mvnw) versionado no repo.

pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 60, unit: 'MINUTES')
    }

    // Polling do SCM a cada 1 minuto (sintaxe cron do Jenkins: min hora dia mês dia-sem).
    triggers {
        pollSCM('* * * * *')
    }

    environment {
        // Testcontainers fala com o Docker do host via socket montado.
        DOCKER_HOST = 'unix:///var/run/docker.sock'
    }

    stages {
        // A esteira inteira só roda para branches release/*.
        stage('CI/CD release') {
            when {
                branch pattern: 'release/.*', comparator: 'REGEXP'
            }

            stages {
                stage('Checkout') {
                    steps {
                        checkout scm
                    }
                }

                // ── CI: build + testes + Sonar + Quality Gate por módulo ──────────
                stage('ms-usuario') {
                    steps {
                        dir('ms-usuario') {
                            sh './mvnw -B clean verify'
                            withSonarQubeEnv('SonarQube') {
                                sh './mvnw -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751:sonar'
                            }
                        }
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }

                stage('ms-curriculum') {
                    steps {
                        dir('ms-curriculum') {
                            sh './mvnw -B clean verify'
                            withSonarQubeEnv('SonarQube') {
                                sh './mvnw -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751:sonar'
                            }
                        }
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }

                stage('ms-vagas') {
                    steps {
                        dir('ms-vagas') {
                            sh './mvnw -B clean verify'
                            withSonarQubeEnv('SonarQube') {
                                sh './mvnw -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751:sonar'
                            }
                        }
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }

                stage('bff-curriculum') {
                    steps {
                        dir('bff-curriculum') {
                            sh './mvnw -B clean verify'
                            withSonarQubeEnv('SonarQube') {
                                sh './mvnw -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751:sonar'
                            }
                        }
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }

                // ── CD: só chega aqui se os 4 Quality Gates passaram ──────────────
                stage('Deploy') {
                    steps {
                        echo 'Quality Gates aprovados — reconstruindo e subindo os containers.'
                        sh '''docker compose build \
                            ms-usuario ms-curriculum ms-vagas bff-curriculum'''
                        sh '''docker compose up -d \
                            curriculum-db keycloak \
                            ms-usuario ms-curriculum ms-vagas bff-curriculum kong'''
                    }
                }
            }
        }
    }

    post {
        always {
            junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
        }
    }
}
