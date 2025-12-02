# 📘 Como Rodar o Projeto

Este guia explica como executar o projeto, que está organizado em **microserviços** e um **BFF (Backend for Frontend)**, utilizando **Docker Compose** e os scripts automáticos fornecidos.  
O processo funciona tanto em **Linux/macOS** quanto em **Windows (via Git Bash ou PowerShell)**.

---

# 🧩 Arquitetura do Projeto — Microserviços + BFF

O projeto segue uma arquitetura baseada em **microserviços**, onde cada serviço possui sua própria responsabilidade, banco de dados e porta de execução.  
O **BFF** centraliza as chamadas e serve como ponto único de acesso para o frontend.

A comunicação funciona assim:

Browser → BFF → Microserviços → MySQL

Todos os serviços se comunicam internamente pela rede Docker `curriculum-net`.

---

# 🏗️ Microserviços Disponíveis

## 📘 ms-curriculum
- URL: http://localhost:8090
- Swagger: http://localhost:8090/swagger-ui/index.html

## 👤 ms-usuario
- URL: http://localhost:8091
- Swagger: http://localhost:8091/swagger-ui/index.html

## 💼 ms-vagas
- URL: http://localhost:8092
- Swagger: http://localhost:8092/swagger-ui/index.html

---

# 🖥️ BFF — Backend for Frontend

- URL: http://localhost:8080
- Frontend acessível via: http://localhost:8080

---

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
