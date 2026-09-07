# Roadmap de Gameplay Futuro

Este documento registra ideias grandes para uma futura reestruturação do
DragonMineManorsZ. Nada aqui representa implementação atual; a intenção é
guardar direção de design, critérios de balanceamento e sistemas que precisam
ser estudados antes de virar patch.

## Objetivo Geral

Transformar a progressão do modpack em uma experiência mais próxima de Dragon
Ball Xenoverse e Dragon Ball Z: Kakarot, com combate mais legível, missões em
arenas controladas, progressão de party confiável e transformações balanceadas
por raça.

## Balanceamento de Transformações

Cada raça deve ter uma curva própria de poder, custo e função em combate. O
objetivo não é deixar todas as raças iguais, mas garantir que todas tenham um
caminho forte e divertido durante a progressão.

Diretrizes iniciais:

- Transformações devem acompanhar o avanço das sagas e não apenas grind livre.
- Cada forma deve ter uma função clara: dano explosivo, resistência, velocidade,
  controle de energia, regeneração ou sustentação longa.
- Formas superiores devem exigir domínio real das anteriores, mas sem prender o
  jogador em horas repetitivas de treino.
- O custo de energia deve crescer junto com o ganho de poder, evitando formas
  finais permanentes sem investimento.
- Raças com menos transformações precisam compensar com qualidade, estabilidade
  ou mecânicas únicas.

## Progressao Ligada as Quests

A progressão de poder deve ser mais conectada ao roteiro das sagas. O jogador
deve sentir que derrotar inimigos importantes, completar treinos e passar por
eventos principais libera novos estágios de evolução.

Ideias:

- Missões principais liberam marcos de transformação.
- Side quests importantes podem acelerar maestria ou liberar técnicas.
- Treinos específicos podem substituir parte do grind de maestria.
- Algumas formas podem exigir conclusão de arco, mestre específico ou arena de
  teste.
- O progresso deve ser compartilhado corretamente quando a missão for feita em
  party.

## Novo Sistema de Combate

O sistema atual de habilidades e combate físico não passa a sensação de luta de
Dragon Ball com clareza suficiente. A meta futura é refazer o combate para
parecer mais com Xenoverse e Kakarot.

Pilares desejados:

- Mira travada em alvo durante combate importante.
- Combos físicos com sequência, abertura e finalização.
- Habilidades de ki com animações, tempo de conjuração e janela de punição.
- Esquiva, perseguição, knockback e reposicionamento com leitura clara.
- Inimigos com comportamento próprio, não apenas atributos altos.
- Feedback visual e sonoro forte para golpes, defesas, quebras e transformações.

## Missoes em Arenas

Ao iniciar certas missões, o jogador deve ser enviado para uma arena preparada,
sozinho ou com party. Isso reduz bugs do mundo aberto, melhora a direção das
lutas e permite controlar inimigos, objetivos e recompensas.

Regras desejadas:

- Missões podem ser iniciadas solo ou em party.
- Todos os membros aceitos entram na arena juntos.
- A arena define inimigos, limites, objetivos e condição de vitória.
- Ao entrar, a mira pode travar automaticamente nos inimigos da missão.
- Ao concluir, falhar ou sair, os jogadores voltam para o ponto correto.
- A arena deve evitar interferência de jogadores externos.

## Party e Progresso Compartilhado

O sistema de party atual precisa ser revisado porque ainda gera muitos bugs de
progresso. A meta é tornar party uma parte central da experiência, não um risco.

Comportamentos esperados:

- Aceitar missão em grupo deve registrar participantes de forma consistente.
- Objetivos de matar inimigos, coletar itens e sobreviver devem sincronizar para
  todos quando fizer sentido.
- Conclusão de saga e side quest deve respeitar os membros ativos da party.
- Jogadores desconectados, mortos ou fora da arena precisam ter regras claras.
- O sistema deve impedir duplicação de recompensa e perda de progresso.

## Mira Travada

Em arenas ou lutas importantes, a mira deve travar em inimigos válidos de forma
parecida com jogos de ação de Dragon Ball.

Ideias iniciais:

- Travar automaticamente no inimigo principal ao iniciar a arena.
- Permitir trocar alvo entre inimigos.
- Destravar ao vencer, sair da arena ou perder linha de combate.
- Ajustar câmera, movimento e habilidades para funcionar bem com lock-on.
- Evitar lock-on fora de combate comum, a menos que o jogador ative manualmente.

## Animacoes de Combate e Armas de Ki

Armas e habilidades de ki precisam ter animações próprias para deixar o combate
mais expressivo.

Prioridades:

- Animações de golpes físicos básicos e combos.
- Animações de carregamento e disparo de ki.
- Animações para armas de ki, lâminas, rajadas e técnicas canalizadas.
- Cancelamentos controlados para esquiva ou defesa.
- Integração entre animação, dano, gasto de energia e hitbox.

## Pendencias de Pesquisa

Antes de implementar, será necessário investigar:

- Quais sistemas do DragonMineZ podem ser alterados por patch sem reescrever o
  mod inteiro.
- Como o sistema atual salva progresso de quests, party e saga.
- Se arenas devem ser dimensões separadas, estruturas instanciadas ou regiões
  protegidas no mundo.
- Qual biblioteca ou abordagem usar para animações de player e entidades.
- Como sincronizar lock-on, câmera e combate entre cliente e servidor.
- Como manter compatibilidade com servidor Forge 1.20.1.
