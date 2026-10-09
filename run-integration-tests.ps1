param(
    [ValidateSet('all', 'ms-usuario', 'ms-curriculum', 'ms-vagas')]
    [string]$Module = 'all'
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Test-CommandExists {
    param([string]$Name)
    return $null -ne (Get-Command $Name -ErrorAction SilentlyContinue)
}

function Find-JavaHome {
    $candidates = @(
        'C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot',
        'C:\Program Files\Microsoft\jdk-21.0.12.100-hotspot',
        'C:\Program Files\Microsoft\jdk-21*',
        'C:\Program Files\Eclipse Adoptium\jdk-*',
        'C:\Program Files\BellSoft\LibericaJDK-*',
        'C:\Program Files\Java\jdk-*'
    )

    foreach ($pattern in $candidates) {
        $matches = Get-ChildItem -Path $pattern -Directory -ErrorAction SilentlyContinue | Sort-Object Name -Descending
        if ($matches) {
            foreach ($m in $matches) {
                $javaBin = Join-Path $m.FullName 'bin\java.exe'
                if (Test-Path $javaBin) {
                    return $m.FullName
                }
            }
        }
    }

    $javaFromPath = (Get-Command java -ErrorAction SilentlyContinue)
    if ($javaFromPath) {
        return Split-Path -Parent (Split-Path -Parent $javaFromPath.Source)
    }

    return $null
}

Write-Host '==> Verificando requisitos...'

if (-not (Test-CommandExists 'docker')) {
    throw 'Docker CLI não foi encontrado no PATH.'
}

$javaHome = Find-JavaHome
if (-not $javaHome) {
    throw 'JDK 21 não foi encontrado. Instale o Microsoft Build of OpenJDK 21.'
}

Write-Host "Usando JAVA_HOME=$javaHome"
$env:JAVA_HOME = $javaHome
$env:PATH = "$javaHome\bin;$env:PATH"

# Tenta usar o contexto do Docker Desktop Linux (mais compatível com Testcontainers no Windows)
$dockerContexts = & docker context ls 2>$null | Out-String
if ($dockerContexts -match 'desktop-linux') {
    Write-Host '==> Usando contexto Docker desktop-linux'
    & docker context use desktop-linux
    if ($LASTEXITCODE -ne 0) {
        throw 'Falha ao ativar o contexto Docker desktop-linux.'
    }
}

Write-Host '==> Validando Docker'
& docker version
if ($LASTEXITCODE -ne 0) {
    throw 'Docker não está acessível. Abra o Docker Desktop e tente novamente.'
}

& docker ps
if ($LASTEXITCODE -ne 0) {
    throw 'Docker está instalado, mas não respondeu a docker ps. Verifique o Docker Desktop.'
}

# Define o host que o Testcontainers geralmente aceita no Windows com Docker Desktop
$env:DOCKER_HOST = 'npipe:////./pipe/dockerDesktopLinuxEngine'
$env:TESTCONTAINERS_HOST_OVERRIDE = 'npipe:////./pipe/dockerDesktopLinuxEngine'

$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$modules = @()

switch ($Module) {
    'all' { $modules = @('ms-usuario', 'ms-curriculum', 'ms-vagas') }
    default { $modules = @($Module) }
}

Write-Host "==> Executando testes de integração: $($modules -join ', ')"

foreach ($mod in $modules) {
    $dir = Join-Path $repoRoot $mod
    if (-not (Test-Path $dir)) {
        Write-Warning "Módulo não encontrado: $mod"
        continue
    }

    Push-Location $dir
    try {
        Write-Host "\n### Rodando $mod ###"
        & .\mvnw.cmd test -Dtest="*IntegrationTest" -DfailIfNoTests=false
        if ($LASTEXITCODE -ne 0) {
            throw "Falha ao executar testes do módulo $mod"
        }
    }
    finally {
        Pop-Location
    }
}

Write-Host '\n==> Todos os testes de integração concluíram com sucesso.'
