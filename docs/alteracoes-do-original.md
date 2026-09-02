# Alterações em relação ao DragonMineZ original

## Gerais e Saiyajin

- `combat.json`: `baselineFormDrain` foi reduzido de `80` para `55`.
- `forms/kaioken.json`: consumo de vida foi removido; o Kaioken usa energia.
  Multiplicadores avançados exigem 100 de maestria em todos os multiplicadores
  anteriores (`x2 → x3 → x4 → x10 → x20`).
- Formas Saiyajin receberam menor drain, melhor ganho de maestria e requisitos
  de 100 na progressão entre formas.
- A Maestria Global Saiyajin foi adicionada por patch de código. O tempo passivo
  estimado para todas as formas Saiyajin caiu de cerca de `552,1 h` para
  `19,7 h`, antes de contar luta e outras fontes de ganho.

## Outras raças

As linhas Humano, Freeza, Majin, Bio-Android, Ultra Instinct e Ultra Ego foram
revisadas a partir do V6. Cada caminho passou a usar ganho por combate de `0,08`
e maestria passiva coerente com o nível da forma.

| Linha | Faixa de ganho passivo / 5 s | Tempo aproximado por forma, só passivo |
| --- | ---: | ---: |
| Formas iniciais | `0,12–0,10` | 1,2–1,4 h |
| Formas intermediárias | `0,09–0,07` | 1,5–2,0 h |
| Formas finais | `0,06–0,04` | 2,3–3,5 h |
| UI e UE | `0,08–0,05` | 1,7–2,8 h |

Essa faixa é deliberadamente mais lenta que o fim da árvore Saiyajin, porque
apenas Saiyajins recebem o multiplicador global de até 10x.
