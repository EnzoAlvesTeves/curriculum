param(
    [ValidateSet('all', 'ms-usuario', 'ms-curriculum', 'ms-vagas')]
    [string]$Module = 'all'
)

$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$scriptInWsl = '/mnt/host/c/r/curriculum/run-integration-tests-wsl.sh'

if (-not (Get-Command wsl.exe -ErrorAction SilentlyContinue)) {
    throw 'WSL não está instalado ou não está no PATH.'
}

& wsl.exe sh "$scriptInWsl" $Module
exit $LASTEXITCODE
