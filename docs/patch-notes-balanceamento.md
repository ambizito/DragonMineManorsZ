# Patch notes — Balanceamento de sagas e formas

## Sagas e inimigos de quests

- Os inimigos ajustados agora usam o mesmo valor para `meleeDamage` e `kiDamage`, evitando que ataques de Ki sejam desproporcionalmente mais fortes ou fracos que ataques físicos.
- A vida e o dano dos inimigos foram arredondados para valores mais fáceis de ler e balancear.
- A Saga Buu foi rebalanceada: inimigos de treino, torneio e chefes receberam dano menor e progressão de vida mais clara. A faixa vai de `200.000` a `1.550.000` de vida e de `1.600` a `9.800` de dano.
- A Saga Future recebeu a mesma faixa de dificuldade da Saga Buu: `200.000–1.550.000` de vida e `1.600–9.800` de dano.
- A Saga GT foi normalizada para `2.300.000–3.350.000` de vida e `14.400–21.000` de dano.
- A Saga Dragon Ball recebeu a mesma faixa de dificuldade da Saga GT: `2.300.000–3.350.000` de vida e `14.400–21.000` de dano.
- As sagas posteriores seguem uma escala crescente de dificuldade:

| Saga | Vida | Dano melee/Ki |
| --- | ---: | ---: |
| GT | 2,30–3,35 M | 14.400–21.000 |
| Beerus | 3,40–3,80 M | 21.200–24.000 |
| ROF | 3,90–4,40 M | 24.200–27.600 |
| U6 | 4,50–5,00 M | 28.000–31.200 |
| Black | 5,10–5,80 M | 32.000–36.000 |
| TOP | 5,90–7,00 M | 37.000–44.000 |

## Missões paralelas e progressão

- Defesa da Cidade de Videl: os 10 inimigos passaram de `1.354.000` de vida e até `17.700` de dano para `250.000` de vida e `1.400` de dano.
- Além de Vegetto: dano físico e de Ki do inimigo foram igualados em `8.400`.
- A missão 23 da Saga Movies (Broly Base) agora só libera após concluir a missão 31 da Saga GT, contra Omega Shenron.
- O multiplicador de treino de SKP foi reduzido de `1,0` para `0,1`, desacelerando especificamente a evolução desse atributo por prática.

## Formas raciais

- Formas de Bio-Android, Frost Demon e Majin foram revisadas.
- Multiplicadores de atributos foram ajustados para reduzir picos excessivos e diferenciar melhor as etapas de evolução.
- Ganho de maestria por golpe e maestria passiva foram reduzidos em diversas formas, tornando o domínio delas mais gradual.
- Limites de multiplicador de stats, Energia, Defesa, Força, SKP e Poder de Ki foram ajustados em formas específicas para manter a progressão entre as transformações.

## Observação

Estas alterações foram feitas apenas em `config/`. A pasta `servedata/` não foi modificada.
