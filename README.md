# 📘 Como Rodar o Projeto

Este guia explica como executar o projeto utilizando **Docker Compose** e o script automatizado fornecido.  
O processo funciona tanto em **Linux/macOS** quanto em **Windows (via Git Bash ou PowerShell)**.

## ✅ Pré-requisitos

### **1. Git**
```bash
git --version
```

### **2. Docker + Docker Compose**
```bash
docker --version
docker compose version
```

### **3. Git Bash (Windows)**
Necessário para rodar scripts `.sh` no Windows.

---

## 🚀 Como rodar o projeto

Existem duas maneiras: **com script automático** ou **manualmente**.

---

# 📂 1. Rodando com o Script Automático

Scripts disponíveis:

- `build_and_run.sh` (Linux/macOS/Git Bash)
- `build_and_run.ps1` (PowerShell)

---

## ▶️ Linux / macOS

```bash
chmod +x build_and_run.sh
./build_and_run.sh
```

## ▶️ Windows (Git Bash)

```bash
./build_and_run.sh
```

## ▶️ Windows (PowerShell)

```powershell
powershell -ExecutionPolicy Bypass -File build_and_run.ps1
```

---

# ⚙️ 2. Rodando Manualmente

```bash
git pull
docker compose build
docker compose up
```

---

# 🛑 Parar o projeto

```bash
docker compose down
docker compose down --remove-orphans --volumes
```
