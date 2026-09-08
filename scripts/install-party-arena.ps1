param([string]$Instance = 'D:\CurseForge\Instances\Dragon Mine Z')
$ErrorActionPreference = 'Stop'
$repo = Split-Path $PSScriptRoot -Parent
$jarName = 'dragonminez-2.1.3-manorsz.0.3.26.9.8.jar'
$sourceJar = Join-Path $repo $jarName
if (!(Test-Path $sourceJar)) { throw 'JAR da versão 0.3.26.9.8 não encontrado.' }
$mods = Join-Path $Instance 'mods'
if (!(Test-Path $mods)) { throw 'Pasta mods do perfil não encontrada.' }
$stamp = Get-Date -Format 'yyyyMMdd-HHmmss'
$backup = Join-Path $Instance "mod-backups/party-arena-$stamp"
New-Item -ItemType Directory -Path $backup -Force | Out-Null
$resolvedMods = [IO.Path]::GetFullPath($mods).TrimEnd('\') + '\'
foreach ($old in Get-ChildItem -LiteralPath $mods -Filter 'dragonminez-*.jar') {
    if (!$old.FullName.StartsWith($resolvedMods,[StringComparison]::OrdinalIgnoreCase)) { throw 'Destino fora de mods.' }
    Move-Item -LiteralPath $old.FullName -Destination $backup
}
Copy-Item -LiteralPath $sourceJar -Destination (Join-Path $mods $jarName)
$profile = Join-Path $Instance 'minecraftinstance.json'
if (Test-Path $profile) {
    Copy-Item -LiteralPath $profile -Destination (Join-Path $backup 'minecraftinstance.json')
    $text = [IO.File]::ReadAllText($profile)
    $text = [regex]::Replace($text,'("isUnlocked"\s*:\s*)false','$1true')
    [IO.File]::WriteAllText($profile,$text)
}
$options = Join-Path $Instance 'options.txt'
if (Test-Path $options) {
    Copy-Item -LiteralPath $options -Destination (Join-Path $backup 'options.txt')
    $text = [IO.File]::ReadAllText($options).Replace('key_key.dragonminez.stats_tab_party:key.keyboard.unknown','key_key.dragonminez.stats_tab_party:key.keyboard.p:ALT')
    [IO.File]::WriteAllText($options,$text)
}
$ftb = Join-Path $Instance 'defaultconfigs/ftbchunks/ftbchunks-world.snbt'
if (Test-Path $ftb) {
    Copy-Item -LiteralPath $ftb -Destination (Join-Path $backup 'ftbchunks-world.snbt')
    $text = [IO.File]::ReadAllText($ftb)
    if ($text -notmatch 'dragonminez:quest_arena') {
        if ($text -match 'claim_dimension_blacklist\s*:\s*\[') {
            $text = [regex]::Replace($text,'claim_dimension_blacklist\s*:\s*\[','claim_dimension_blacklist: ["dragonminez:quest_arena", ')
        } else {
            $text = $text.Insert($text.IndexOf('{') + 1, "`n    claim_dimension_blacklist: [`"dragonminez:quest_arena`"]`n")
        }
        [IO.File]::WriteAllText($ftb,$text)
    }
} else {
    New-Item -ItemType Directory -Path (Split-Path $ftb) -Force | Out-Null
    Copy-Item -LiteralPath (Join-Path $repo 'defaultconfigs/ftbchunks/ftbchunks-world.snbt') -Destination $ftb
}
Get-FileHash -Algorithm SHA256 -LiteralPath (Join-Path $mods $jarName)
Write-Output "Instalado. Backup: $backup"
