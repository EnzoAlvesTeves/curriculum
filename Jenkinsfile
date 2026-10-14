// Esteira CI/CD local para branches release/*.
//
// Fluxo (fases agrupadas, build reaproveitado entre as fases):
//   Checkout → Build → Testes Unitários → Testes de Integração
//            → Análise Sonar + Quality Gate → Deploy
//
// Reaproveitamento: só a fase Build roda `clean` (compila + empacota uma vez
// com install -DskipTests). As fases seguintes NÃO chamam `clean`, então o
// Maven reusa o target/ já compilado em vez de recompilar.
//
// Separação de testes (os projetos não usam Failsafe; a distinção é por nome):
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
        // Lista de módulos Maven, reutilizada nas fases via loop.
        MODULOS = 'ms-usuario ms-curriculum ms-vagas bff-curriculum'
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

                // ── Build: compila e empacota uma vez (sem testes). Único `clean`. ──
                stage('Build') {
                    steps {
                        script {
                            for (m in env.MODULOS.split()) {
                                dir(m) {
                                    sh './mvnw -B clean install -DskipTests'
                                }
                            }
                        }
                    }
                }

                // ── Testes unitários: reusa o target/ (sem clean) ──────────────────
                stage('Testes Unitários') {
                    steps {
                        script {
                            for (m in env.MODULOS.split()) {
                                dir(m) {
                                    sh "./mvnw -B test -Dtest='!*IntegrationTest' -DfailIfNoTests=false"
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

                // ── Testes de integração: reusa o target/ (sem clean) ──────────────
                // bff-curriculum não tem *IntegrationTest; o filtro simplesmente não acha nada.
                stage('Testes de Integração') {
                    steps {
                        script {
                            for (m in ['ms-usuario', 'ms-curriculum', 'ms-vagas']) {
                                dir(m) {
                                    sh "./mvnw -B test -Dtest='*IntegrationTest' -DfailIfNoTests=false"
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

                // ── Análise Sonar + Quality Gate bloqueante (por módulo) ───────────
                // Reusa o target/ (incluindo o jacoco.xml gerado nas fases de teste).
                stage('Análise Sonar') {
                    steps {
                        script {
                            for (m in env.MODULOS.split()) {
                                dir(m) {
                                    withSonarQubeEnv('SonarQube') {
                                        sh "./mvnw -B ${env.SONAR_PLUGIN}"
                                    }
                                }
                                timeout(time: 10, unit: 'MINUTES') {
                                    waitForQualityGate abortPipeline: true
                                }
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
