param(
    [string]$BuildDirectory,
    [string]$JavaHome = $env:JAVA_HOME,
    [switch]$GameTests
)
$ErrorActionPreference = 'Stop'
$repo = Split-Path $PSScriptRoot -Parent
if (!$BuildDirectory) { $BuildDirectory = Join-Path $repo '.build/dragonminez-0.3.26.9.8' }
if ($JavaHome) { $env:JAVA_HOME = $JavaHome }
if (!(Test-Path (Join-Path $BuildDirectory 'gradlew.bat'))) {
    if (Test-Path $BuildDirectory) { throw 'Use uma pasta de build vazia/inexistente ou um checkout já preparado por este script.' }
    git clone https://github.com/DragonMineZ/dragonminez.git $BuildDirectory
    if ($LASTEXITCODE -ne 0) { throw 'Falha ao obter o código oficial.' }
    git -C $BuildDirectory checkout --detach 3c58ae869788a37fb42d0ed47a5494724a07735a
    if ($LASTEXITCODE -ne 0) { throw 'Falha ao selecionar a base 2.1.3.' }
}
$head = git -C $BuildDirectory rev-parse HEAD
if ($head -ne '3c58ae869788a37fb42d0ed47a5494724a07735a') { throw 'O checkout de build não está no commit base documentado.' }
Copy-Item -Path (Join-Path $repo 'source/dragonminez/*') -Destination $BuildDirectory -Recurse -Force
Push-Location $BuildDirectory
try {
    if ($GameTests) {
        & .\gradlew.bat runGameTestServer -PpartyGameTests=true -PoptimizeResources=false --console=plain
    } else {
        & .\gradlew.bat build -PoptimizeResources=false --console=plain
    }
    if ($LASTEXITCODE -ne 0) { throw 'A compilação/validação falhou.' }
} finally { Pop-Location }
if (!$GameTests) {
    $jar = Join-Path $BuildDirectory 'build/libs/dragonminez-2.1.3-manorsz.0.3.26.9.8.jar'
    if (!(Test-Path $jar)) { throw 'JAR final não encontrado.' }
    Get-FileHash -Algorithm SHA256 -LiteralPath $jar
}
