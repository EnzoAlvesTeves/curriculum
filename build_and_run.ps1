Write-Host "🔄 Git Pull..."
git pull

Write-Host "🔨 Docker Compose Build..."
docker compose build

Write-Host "🚀 Docker Compose Up..."
docker compose up
