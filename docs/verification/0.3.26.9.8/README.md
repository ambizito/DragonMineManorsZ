# Validação da versão 0.3.26.9.8

## Artefato

`dragonminez-2.1.3-manorsz.0.3.26.9.8.jar`

SHA-256: `6C1F41F18C54EE7AD759278AAE46B494BF32E426871C2CF58F1D65EFFD6FE118`

Compilado com Java 17 e `gradlew build -PoptimizeResources=false --offline`.
O JAR contém a HUD, o gerenciador de arenas, os JSONs da dimensão e as
dependências empacotadas. As classes de teste não estão no artefato de produção.

## Testes automatizados no Minecraft/Forge

Comando: `gradlew runGameTestServer -PpartyGameTests=true -PoptimizeResources=false --offline`.

Resultado: **All 5 required tests passed**.

1. `sharedBattleRewardsAndReturn`: membro que não é líder inicia, toda a party
   entra, apoio sem desbloqueio contribui para kills, duplicação de kill é
   ignorada, somente elegíveis recebem recompensa, recibos sobrevivem ao NBT
   e cada jogador retorna ao ponto original.
2. `departureDoesNotStrandRemainingMembers`: quem iniciou sai da party, perde
   a participação e não recebe recompensa; os restantes terminam e retornam.
3. `hudPacketAndArenaPersistence`: dados da HUD sobrevivem à codificação de
   rede, slots não se repetem após salvar/carregar, dimensão e estrutura Cell
   existem no servidor.
4. `requirementsDeathAndLogout`: dimensão exigida continua bloqueando o início;
   morte conserva o bilhete para respawn, a missão pode ser reiniciada e
   desconectar encerra a participação sem premiar a falha.
5. `combinedInventoryCompletesForBoth`: uma coleta usa itens distribuídos entre
   dois inventários e conclui para ambos sem levar a missão de coleta à arena.

Os testes usam jogadores simulados do Forge, em mundos de desenvolvimento
isolados. Não substituem uma sessão longa entre jogadores humanos, nem simulam
uma queda de energia durante a gravação de disco.

## Cliente e servidor do modpack

- Cliente Forge inicializou, abriu um mundo de teste e renderizou a HUD real.
  [Captura conferida](party-hud.png): dados de membros simulados exclusivamente
  para verificar disposição, textos e barras. Esses nomes/valores não fazem
  parte do JAR de produção.
- Servidor dedicado com **89 mods** correspondentes ao conjunto de servidor
  presente no perfil do usuário chegou a `Done (81.551s)` com o JAR de produção.
  O teste foi executado apenas em `127.0.0.1:25575`, com mundo separado.
- O carregamento registrou avisos de receitas/tags de outros addons
  (Mekanism, Creating Space e Tinkers). A inicialização foi concluída.
- Os cinco patches aplicaram em sequência sobre o commit oficial
  `3c58ae869788a37fb42d0ed47a5494724a07735a`.

Os logs completos permanecem nas pastas de build locais. Esta pasta contém
os trechos de resultados e a captura necessários para revisar a entrega.
