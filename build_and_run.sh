#!/usr/bin/env bash

echo "🔄 Git Pull..."
git pull

echo "🔨 Docker Compose Build..."
docker compose build

echo "🚀 Docker Compose Up..."
docker compose up
