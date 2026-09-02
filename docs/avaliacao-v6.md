# Avaliação do arquivo `dragonminezV6.zip`

O V6 foi usado como referência de intenção: menos grind, menos gasto de energia
e domínio completo antes de avançar. Ele não foi copiado literalmente, porque
alguns valores gerariam progressão quase instantânea ou inconsistente.

## Incorporado e refinado

- Exigência de `100` de maestria para formas que dependem da anterior.
- Ganho por combate maior que o original: `0,04 → 0,08`.
- Ultra Instinct e Ultra Ego passam a ter ganho de maestria, que era `0` no
  original.
- Reduções de drain em formas avançadas foram mantidas de maneira gradual.
- Bio-Android, Humano, Freeza e Majin receberam progressão de maestria por
  estágio, em vez de um valor idêntico ou aleatório por forma.

## Corrigido para coerência

No V6, diversos valores de `passiveMasteryEveryFiveSeconds` estavam entre
`0,50` e `0,96`. Com maestria máxima 100, `0,96` por 5 s completa uma forma em
aproximadamente **8,7 minutos**. Isso não combina com exigir 100 para liberar a
forma seguinte. Nesta configuração, os valores foram normalizados para
`0,04–0,12`.

Também havia drains como `0,002`, `0,0032` e `0,008` em formas muito fortes,
enquanto outras formas equivalentes mantinham custo alto. Eles foram ajustados
para uma curva simples: início barato, meio moderado e fim mais caro.

## Não incorporado

- `general-user.json`: `hexagonStatsDisplay: true` é preferência visual de cada
  cliente, não configuração de balanceamento do servidor.
- `races/ultimatehybrid/forms/ultimatehybridforms.json`: o V6 alterava apenas
  alguns ramos, deixando outros no padrão e criando diferenças extremas dentro
  da mesma raça. Foi mantido o original até que a árvore inteira receba uma
  proposta própria.
- Alterações duplicadas de Saiyajin, combate e Kaioken do V6 foram substituídas
  pela versão final deste repositório, que inclui o sistema de Maestria Global
  e Kaioken por energia.
