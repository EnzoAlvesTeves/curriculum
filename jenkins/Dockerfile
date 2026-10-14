# Jenkins LTS com JDK 21 + Docker CLI, para orquestrar os builds Maven
# dos microserviços (que usam Testcontainers e, portanto, precisam falar
# com o Docker do host via socket montado).
FROM jenkins/jenkins:lts-jdk21

USER root

# Docker CLI (apenas o cliente; o daemon é o do host, acessado via /var/run/docker.sock)
# e utilitários básicos.
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        ca-certificates curl gnupg lsb-release \
    && install -m 0755 -d /etc/apt/keyrings \
    && curl -fsSL https://download.docker.com/linux/debian/gpg \
        | gpg --dearmor -o /etc/apt/keyrings/docker.gpg \
    && chmod a+r /etc/apt/keyrings/docker.gpg \
    && echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
        https://download.docker.com/linux/debian $(lsb_release -cs) stable" \
        > /etc/apt/sources.list.d/docker.list \
    && apt-get update \
    && apt-get install -y --no-install-recommends docker-ce-cli docker-compose-plugin \
    && rm -rf /var/lib/apt/lists/*

# Plugins instalados na construção da imagem (evita setup manual pela UI).
RUN jenkins-plugin-cli --plugins \
    workflow-aggregator \
    git \
    sonar \
    pipeline-stage-view \
    docker-workflow \
    configuration-as-code

USER jenkins
