#!/usr/bin/env bash

echo "Detectando sistema operacional..."

OS=$(uname -s | tr '[:upper:]' '[:lower:]')

if [[ "$OS" == *"mingw"* || "$OS" == *"msys"* || "$OS" == *"cygwin"* ]]; then
    echo "➡️ Rodando no Windows (Git Bash)"
else
    echo "➡️ Rodando no Linux/Mac"
fi

echo "🔄 Git Pull..."
git pull

echo "🔨 Docker Compose Build..."
docker compose build

echo "🚀 Docker Compose Up..."
docker compose up