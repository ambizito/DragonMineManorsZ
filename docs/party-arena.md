# Party e arenas — 0.3.26.9.8

A party é a do **DragonMineZ**, acessível pelo menu de personagem ou por
**Alt + P**. FTB Teams continua cuidando dos terrenos; não substitui esta party.

## Como jogar

1. Convide os amigos pelo menu de party e aguarde a aceitação dos convites.
2. Qualquer membro pode iniciar uma quest disponível para ele. Bioma, dimensão,
   nível e demais requisitos de início continuam sendo verificados no local.
3. Missões que invocam inimigos transportam todos os membros online para
   `dragonminez:quest_arena`. Cada batalha recebe uma região própria, com uma
   cópia da estrutura original do Cell e terreno que pode ser destruído.
4. Qualquer participante pode ajudar a derrotar os inimigos. Quem tinha a mesma
   quest disponível ao entrar recebe o progresso e a conclusão; quem já a fez
   ou ainda não a desbloqueou entra como apoio, sem ganhar conclusão/recompensa.
5. Recompensas normais são entregues automaticamente e têm registro individual
   para impedir resgate repetido. Recompensas exclusivas de NPC continuam
   vinculadas à entrega no NPC.
6. Terminada a batalha, cada pessoa volta à sua dimensão e posição de partida.
   Se ainda houver uma entrega no NPC ou um objetivo no mundo, a quest permanece
   em andamento para concluir essa etapa depois do retorno.

Quests de exploração, coleta ou caça a criaturas naturais continuam no mundo
onde seus objetivos existem. A mudança de arena se aplica às batalhas com
inimigos invocados pela quest. Os requisitos e os arquivos das quests não foram
reescritos.

## HUD e compartilhamento

A HUD no canto inferior esquerdo mostra nick, líder, vida atual/máxima, barra
de vida, distância e os estados offline, derrotado, arena ou outra dimensão.
Os dados vêm do servidor duas vezes por segundo, inclusive para companheiros
fora da distância de renderização. A HUD acompanha a escala da interface e
oculta-se com F1, tela de depuração e chat aberto. Partys maiores alternam as
páginas de membros automaticamente.

Entrar na party, trocar de líder ou reconectar não copia o histórico de quests.
Ao iniciar uma missão, a elegibilidade dos participantes é avaliada
individualmente. Mortes de inimigos, interações e invocações contribuem para
as quests aceitas; objetivos de itens contam o inventário combinado dos
membros ativos. Não é necessário que o líder dê o último golpe.

## Saída, falha e recuperação

- `/dmzarena sair` abandona a participação e devolve o jogador ao ponto de origem.
- Morrer, desconectar, sair da party, sair da dimensão ou ultrapassar os limites
  da batalha encerra a participação, sem recompensa. Na morte, o retorno ocorre
  no respawn. Os demais membros elegíveis podem continuar a luta.
- Entrar na party durante uma batalha não inclui alguém retroativamente nela.
- Há um limite de 30 minutos por batalha. Sem participantes elegíveis, a arena
  é encerrada e os acompanhantes retornam.
- O botão de reinvocar não cria ondas duplicadas durante uma batalha ativa.
  Para tentar novamente, abandone a arena e reinicie a missão no local exigido.
- O servidor salva os bilhetes de retorno antes do teleporte. Após reiniciar
  o servidor, uma batalha interrompida é tratada como falha, com retorno ao
  reconectar. As recompensas já registradas continuam pessoais.

As regras normais de morte e inventário do modpack continuam em vigor.
O terreno de uma batalha encerrada não é reutilizado: a próxima recebe outra
região. Não há reconstrução contínua nem arenas temáticas novas nesta versão.

## Instalação e código

Use o mesmo JAR no cliente e no servidor. O protocolo de rede desta versão
recusa conexões de versões antigas, evitando pacotes incompatíveis da HUD.
O mod mantém o identificador `dragonminez` para preservar mundos e addons.

O código modificado integral fica em `source/dragonminez/`, como uma sobreposição
do código oficial no commit `3c58ae869788a37fb42d0ed47a5494724a07735a`.
`scripts/build-party-arena.ps1` recria o projeto e compila o JAR.
O patch de party/arena também fica em `patches/`, depois dos quatro anteriores.

O FTB Chunks recebe `claim_dimension_blacklist: ["dragonminez:quest_arena"]`
para impedir que as arenas virem terrenos protegidos. Essa opção existe na
versão instalada; sua finalidade também consta nos
[recursos oficiais do FTB Chunks](https://github.com/FTBTeam/FTB-Chunks/blob/main/common/src/main/resources/assets/ftbchunks/lang/en_us.json).

Os testes de integração ficam em `src/partyTest` da sobreposição e são ativados
somente por `-PpartyGameTests=true`; não fazem parte do JAR de produção. Eles
usam o [GameTest do Forge](https://docs.minecraftforge.net/en/1.20.x/misc/gametest/)
com jogadores simulados dentro do servidor Minecraft.
