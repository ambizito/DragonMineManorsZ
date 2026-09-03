# Administração, proteção e teleportes

Este pacote já inclui **FTB Chunks**, **FTB Teams**, **FTB Essentials**,
**Waystones**, **SecurityCraft** e **WorldEdit**. Não é necessário instalar
WorldGuard ou outro mod apenas para proteger áreas.

## Dar acesso de administrador

Pelo console da hospedagem, sem a barra inicial:

```text
op NomeDoJogador
```

Dentro do jogo, comandos administrativos usam `/`. Para retirar o acesso:

```text
deop NomeDoJogador
```

## Proteger o spawn

O `server.properties` vem com `spawn-protection=16`. Isso protege a área ao redor
do spawn do Overworld contra alterações feitas por jogadores sem OP. Defina o
centro da área estando no local desejado:

```text
/setworldspawn
```

Para mudar o tamanho, pare o servidor e altere `spawn-protection` em
`server.properties`. Use `0` somente se quiser desativar essa proteção.

## Proteger qualquer área com FTB Chunks

O FTB Chunks protege blocos, contêineres, entidades, pistões e explosões dentro
de chunks reivindicados. Cada chunk mede 16 x 16 blocos.

Pelo mapa:

1. Abra o inventário e clique no ícone azul do mapa, ou abra o mapa do FTB
   Chunks.
2. Clique e arraste com o botão esquerdo para reivindicar chunks.
3. Clique com o botão direito para remover uma reivindicação.
4. Use Shift + clique para manter um chunk carregado.

Por comando:

```text
/ftbchunks claim
/ftbchunks claim 64
/ftbchunks unclaim
/ftbchunks info
/ftbchunks admin bypass_protection
```

`claim 64` protege os chunks dentro do raio informado em blocos, tomando como
centro a posição atual. Uma área reivindicada pela conta do administrador fica
indisponível para edição pelos demais jogadores; membros ou aliados da equipe
podem receber acesso pelo menu do FTB Teams.

Para reivindicar em nome de uma equipe já existente, use Tab para completar o
nome:

```text
/ftbchunks admin claim_as <equipe> 64
/ftbchunks admin unclaim_as <equipe> 64
```

Os padrões preparados permitem 100 chunks reivindicados e 10 chunks mantidos
carregados por jogador/equipe, com limites absolutos de 500 e 25 por equipe.

## Limitar o tamanho do mapa

Os comandos vanilla abaixo criam uma borda de 20.000 blocos de diâmetro:

```text
/worldborder center 0 0
/worldborder set 20000
```

Troque o diâmetro conforme necessário. A borda não apaga terreno já gerado.

## Teleporte dos jogadores

O FTB Essentials está configurado com três casas por jogador, aquecimento para
evitar fuga instantânea de combate e os seguintes comandos:

```text
/sethome <nome>
/home [nome]
/delhome [nome]
/listhomes
/spawn
/back
/tpa <jogador>
/tpahere <jogador>
/tpaccept
/tpdeny
/rtp
/warp <nome>
/listwarps
```

`/rtp` funciona somente no Overworld, entre 1.000 e 15.000 blocos do spawn, e
tem intervalo de 10 minutos. Warps globais são criados e removidos por OP:

```text
/setwarp <nome>
/delwarp <nome>
```

## Dimensões

As dimensões são registradas automaticamente pelos mods ao iniciar o servidor;
não existe uma pasta de mundo que precise ser enviada previamente. Portais e
progressão continuam sendo a forma normal de acesso.

Um OP pode abrir/testar uma dimensão com:

```text
/tpx <dimensão>
```

Exemplos importantes:

```text
/tpx dragonminez:namek
/tpx dragonminez:otherworld
/tpx dragonminez:sacredkaiplanet
/tpx dragonminez:time_chamber
/tpx twilightforest:twilight_forest
/tpx dmzsparking:future_earth
/tpx dmzsparking:beerus_planet
/tpx dmzplus:cereal
/tpx dmzplus:vegeta
/tpx dmzplus:vampa
/tpx dmzplus:yardrat
```

Digite `/tpx ` e pressione Tab para ver os identificadores disponíveis no
servidor. O comando mantém as coordenadas atuais; use com cautela porque certas
dimensões podem não ter terreno seguro nessas coordenadas. Para enviar outro
jogador com coordenadas definidas:

```text
/execute in dragonminez:namek run tp NomeDoJogador 0 120 0
```

## Waystones

O teleporte entre dimensões está permitido. Waystones geradas naturalmente são
inquebráveis, somente o proprietário pode renomear uma Waystone colocada e
somente jogadores em criativo podem torná-la global.

## Onde editar as configurações

- `server.properties`: spawn, whitelist, PvP, distância de visão e borda básica.
- `world/serverconfig/ftbchunks-world.snbt`: proteção e limites de chunks do
  mundo ativo.
- `world/serverconfig/ftbessentials.snbt`: homes, TPA, RTP, warps e cooldowns.
- `config/waystones-common.toml`: regras das Waystones.
- `defaultconfigs/`: modelos aplicados automaticamente a mundos novos.

Se `level-name` for diferente de `world`, copie os dois arquivos de
`world/serverconfig` para `<nome-do-mundo>/serverconfig`. Sempre pare o servidor
e faça backup antes de alterar esses arquivos.

## Whitelist

Para permitir somente jogadores cadastrados:

```text
whitelist on
whitelist add NomeDoJogador
whitelist list
```

Esses comandos também podem ser usados pelo console sem a barra `/`.
