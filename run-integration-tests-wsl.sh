#!/bin/sh
set -eu

module="${1:-all}"
script_dir="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"

case "$module" in
  all)
    modules="ms-usuario ms-curriculum ms-vagas"
    ;;
  ms-usuario|ms-curriculum|ms-vagas)
    modules="$module"
    ;;
  *)
    echo "Uso: $0 [all|ms-usuario|ms-curriculum|ms-vagas]"
    exit 1
    ;;
esac

echo "==> Verificando Docker..."
if ! docker info >/dev/null 2>&1; then
  if [ -S /var/run/docker.sock ]; then
    export DOCKER_HOST=unix:///var/run/docker.sock
  else
    export DOCKER_HOST=tcp://host.docker.internal:2375
  fi
fi

if ! docker info >/dev/null 2>&1; then
  echo "Docker não está acessível no WSL."
  echo "Confirme que o Docker Desktop está aberto, com WSL integration ativado, ou use o endpoint host.docker.internal:2375."
  exit 1
fi

if ! docker ps >/dev/null 2>&1; then
  echo "Docker está instalado, mas o daemon não respondeu a docker ps."
  exit 1
fi

echo "==> Verificando Java 21..."
JAVA_HOME=""
if command -v java >/dev/null 2>&1; then
  JAVA_HOME="$(dirname "$(dirname "$(readlink -f "$(command -v java)")")")"
fi

if [ -z "$JAVA_HOME" ]; then
  for candidate in /usr/lib/jvm/* /usr/lib64/jvm/*; do
    if [ -x "$candidate/bin/java" ]; then
      JAVA_HOME="$candidate"
      break
    fi
  done
fi

if [ -z "$JAVA_HOME" ] || [ ! -x "$JAVA_HOME/bin/java" ]; then
  echo "JDK 21 não foi encontrado."
  echo "No Ubuntu/WSL, execute: sudo apt-get update && sudo apt-get install -y openjdk-21-jdk"
  exit 1
fi

export JAVA_HOME
export PATH="$JAVA_HOME/bin:$PATH"
echo "JAVA_HOME=$JAVA_HOME"

for mod in $modules; do
  dir="$script_dir/$mod"
  if [ ! -d "$dir" ]; then
    echo "Módulo não encontrado: $mod"
    continue
  fi

  echo "\n### Rodando $mod ###"
  (
    cd "$dir"
    ./mvnw test -Dtest="*IntegrationTest" -DfailIfNoTests=false
  )
  if [ $? -ne 0 ]; then
    echo "Falha ao executar testes do módulo $mod"
    exit 1
  fi
done

echo "\n==> Todos os testes de integração concluíram com sucesso."
