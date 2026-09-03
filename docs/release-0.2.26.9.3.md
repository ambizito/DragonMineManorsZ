# DragonMineManorsZ 0.2.26.9.3

Hotfix do cliente para Minecraft 1.20.1, Forge 47.4.10 e Java 17.

## Correção

- Corrige o crash `NullPointerException` em
  `MovementSkillsHandler.onClientTick` ao entrar em um servidor.
- O manipulador de movimento agora ignora com segurança os ticks de transição
  em que `LocalPlayer.input` ainda não foi inicializado.
- Mantém todos os balanceamentos, a Maestria Global Saiyajin, a progressão do
  Kaioken e o controle de spawn das Esferas do Dragão da versão anterior.

## Arquivos da release

- `dragonminez-2.1.3.jar`: JAR corrigido, obrigatório nos clientes e servidor.
- `DragonMineManorsZ-client-0.2.26.9.3.zip`: atualização dos jogadores.
- `DragonMineManorsZ-server-0.2.26.9.3.zip`: pacote completo do servidor.
- `SHA256SUMS-0.2.26.9.3.txt`: hashes para conferir os downloads.

## SHA-256

```text
3402661235C516DB1404326F0D992EDFFA743E246D8DEAE48DEB3D786E3CB0A7  dragonminez-2.1.3.jar
A5737163FB5CEA729E889F581A0E3A0EAF965E5E5FD81A8633A0F6407C153F6F  DragonMineManorsZ-client-0.2.26.9.3.zip
C3904C148E6CC20FF9445403FC9770DE1FBABA21791B7752637521ED9FBB287D  DragonMineManorsZ-server-0.2.26.9.3.zip
```
