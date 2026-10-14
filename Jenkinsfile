// Esteira CI/CD local para branches release/*.
//
// Estrutura: um bloco por módulo (Build → Unitários → Integração → Sonar),
// seguido do Deploy. Execução sequencial.
//
//   ms-usuario    → Build → Unitários → Integração → Sonar
//   ms-curriculum → Build → Unitários → Integração → Sonar
//   ms-vagas      → Build → Unitários → Integração → Sonar
//   bff-curriculum→ Build → Unitários → Sonar   (não tem testes de integração)
//   Deploy
//
// Reaproveitamento: dentro de cada módulo só o Build roda `clean` (compila +
// empacota com install -DskipTests). As fases de teste e o Sonar NÃO chamam
// `clean`, reusando o target/ já compilado. O JaCoCo acumula a cobertura entre
// unitários e integração (append=true nos poms).
//
// Separação de testes (sem Failsafe; distinção por nome):
//   - unitários:  -Dtest='!*IntegrationTest'
//   - integração: -Dtest='*IntegrationTest'
//
// Pré-requisitos no Jenkins (ver README.md, seção "Esteira CI/CD"):
//   - Servidor SonarQube cadastrado com o nome 'SonarQube' e o token 'sonar-token'
//   - Webhook no SonarQube → http://jenkins:8080/sonarqube-webhook/
//   - Socket do Docker montado (Testcontainers e deploy via docker compose)

pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 60, unit: 'MINUTES')
    }

    triggers {
        pollSCM('* * * * *')   // polling do SCM a cada 1 minuto
    }

    environment {
        DOCKER_HOST = 'unix:///var/run/docker.sock'
        SONAR_PLUGIN = 'org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751:sonar'
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

                // ── ms-usuario ─────────────────────────────────────────────────────
                stage('ms-usuario') {
                    stages {
                        stage('Build') {
                            steps { dir('ms-usuario') { sh './mvnw -B clean install -DskipTests' } }
                        }
                        stage('Testes Unitários') {
                            steps { dir('ms-usuario') { sh "./mvnw -B test -Dtest='!*IntegrationTest' -DfailIfNoTests=false" } }
                            post { always { junit testResults: 'ms-usuario/target/surefire-reports/*.xml', allowEmptyResults: true } }
                        }
                        stage('Testes de Integração') {
                            steps { dir('ms-usuario') { sh "./mvnw -B test -Dtest='*IntegrationTest' -DfailIfNoTests=false" } }
                            post { always { junit testResults: 'ms-usuario/target/surefire-reports/*.xml', allowEmptyResults: true } }
                        }
                        stage('Análise Sonar') {
                            steps {
                                dir('ms-usuario') { withSonarQubeEnv('SonarQube') { sh "./mvnw -B ${env.SONAR_PLUGIN}" } }
                                timeout(time: 10, unit: 'MINUTES') { waitForQualityGate abortPipeline: true }
                            }
                        }
                    }
                }

                // ── ms-curriculum ──────────────────────────────────────────────────
                stage('ms-curriculum') {
                    stages {
                        stage('Build') {
                            steps { dir('ms-curriculum') { sh './mvnw -B clean install -DskipTests' } }
                        }
                        stage('Testes Unitários') {
                            steps { dir('ms-curriculum') { sh "./mvnw -B test -Dtest='!*IntegrationTest' -DfailIfNoTests=false" } }
                            post { always { junit testResults: 'ms-curriculum/target/surefire-reports/*.xml', allowEmptyResults: true } }
                        }
                        stage('Testes de Integração') {
                            steps { dir('ms-curriculum') { sh "./mvnw -B test -Dtest='*IntegrationTest' -DfailIfNoTests=false" } }
                            post { always { junit testResults: 'ms-curriculum/target/surefire-reports/*.xml', allowEmptyResults: true } }
                        }
                        stage('Análise Sonar') {
                            steps {
                                dir('ms-curriculum') { withSonarQubeEnv('SonarQube') { sh "./mvnw -B ${env.SONAR_PLUGIN}" } }
                                timeout(time: 10, unit: 'MINUTES') { waitForQualityGate abortPipeline: true }
                            }
                        }
                    }
                }

                // ── ms-vagas ───────────────────────────────────────────────────────
                stage('ms-vagas') {
                    stages {
                        stage('Build') {
                            steps { dir('ms-vagas') { sh './mvnw -B clean install -DskipTests' } }
                        }
                        stage('Testes Unitários') {
                            steps { dir('ms-vagas') { sh "./mvnw -B test -Dtest='!*IntegrationTest' -DfailIfNoTests=false" } }
                            post { always { junit testResults: 'ms-vagas/target/surefire-reports/*.xml', allowEmptyResults: true } }
                        }
                        stage('Testes de Integração') {
                            steps { dir('ms-vagas') { sh "./mvnw -B test -Dtest='*IntegrationTest' -DfailIfNoTests=false" } }
                            post { always { junit testResults: 'ms-vagas/target/surefire-reports/*.xml', allowEmptyResults: true } }
                        }
                        stage('Análise Sonar') {
                            steps {
                                dir('ms-vagas') { withSonarQubeEnv('SonarQube') { sh "./mvnw -B ${env.SONAR_PLUGIN}" } }
                                timeout(time: 10, unit: 'MINUTES') { waitForQualityGate abortPipeline: true }
                            }
                        }
                    }
                }

                // ── bff-curriculum (sem testes de integração) ───────────────────────
                stage('bff-curriculum') {
                    stages {
                        stage('Build') {
                            steps { dir('bff-curriculum') { sh './mvnw -B clean install -DskipTests' } }
                        }
                        stage('Testes Unitários') {
                            steps { dir('bff-curriculum') { sh "./mvnw -B test -Dtest='!*IntegrationTest' -DfailIfNoTests=false" } }
                            post { always { junit testResults: 'bff-curriculum/target/surefire-reports/*.xml', allowEmptyResults: true } }
                        }
                        stage('Análise Sonar') {
                            steps {
                                dir('bff-curriculum') { withSonarQubeEnv('SonarQube') { sh "./mvnw -B ${env.SONAR_PLUGIN}" } }
                                timeout(time: 10, unit: 'MINUTES') { waitForQualityGate abortPipeline: true }
                            }
                        }
                    }
                }

                // ── CD: só chega aqui se os 4 Quality Gates passaram ───────────────
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
}
