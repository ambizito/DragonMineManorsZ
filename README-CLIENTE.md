# DragonMineManorsZ — atualização do cliente

Versão **0.3.26.9.8**, para Minecraft 1.20.1, Forge 47.4.10 e Java 17.

Esta versão adiciona HUD de party, progresso compartilhado e batalhas na
dimensão de arenas. Consulte [como jogar](docs/party-arena.md).

## Instalação pelo CurseForge

1. Feche o Minecraft e faça backup dos seus mundos.
2. No CurseForge, abra **Opções do perfil** e ative **Permitir gerenciamento de
   conteúdo para este perfil**.
3. Abra a pasta da instância do modpack.
4. Execute o instalador abaixo a partir deste repositório, ou retire o JAR antigo
   de `mods` e copie `dragonminez-2.1.3-manorsz.0.3.26.9.8.jar` para essa pasta.
5. O instalador salva o JAR e as configurações anteriores em `mod-backups`.
6. Verifique que existe somente um JAR do DragonMineZ na pasta `mods`.
7. Inicie o modpack pelo CurseForge.

Na instalação usada para desenvolver este pacote, a raiz é:

```powershell
.\scripts\install-party-arena.ps1 -Instance 'D:\CurseForge\Instances\Dragon Mine Z'
```

O instalador define **Alt + P** para abrir a party se o atalho estava sem tecla.
Ele também habilita o gerenciamento de conteúdo do perfil CurseForge, com backup
do arquivo de metadados anterior.
Nos mundos já existentes, acrescente `dragonminez:quest_arena` à lista
`claim_dimension_blacklist` de `<mundo>/serverconfig/ftbchunks-world.snbt`.
O pacote `servedata` já contém essa configuração para o servidor.

## Multiplayer

Todos os jogadores e o servidor devem usar o mesmo
`dragonminez-2.1.3-manorsz.0.3.26.9.8.jar`. As configurações administrativas específicas do
servidor estão na pasta `servedata` deste repositório.

O hash do JAR fica em `SHA256SUMS-0.3.26.9.8.txt`.
Para reverter, feche o jogo e restaure o JAR e as configurações do backup
em cliente e servidor. Não apague os mundos.
