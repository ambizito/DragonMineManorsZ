# Especificação das configurações alteradas

Os JSONs seguem a estrutura do DragonMineZ 2.1.3. Estes são os campos usados no
balanceamento.

| Campo | Função | Política deste repositório |
| --- | --- | --- |
| `energyDrain` | consumo contínuo de energia da forma | menor nas formas iniciais; maior nas avançadas |
| `healthDrain` | consumo contínuo de vida | Kaioken usa `0`; o custo foi transferido para energia |
| `passiveMasteryEveryFiveSeconds` | maestria passiva a cada 5 s | `0,04–0,12` nas formas normais; menor em formas finais |
| `masteryPerHitDealt` | maestria por golpe dado | `0,08` nas linhas retrabalhadas |
| `masteryPerHitReceived` | maestria por golpe recebido | `0,08` nas linhas retrabalhadas |
| `unlockOnMastery` | maestria exigida pelo pré-requisito | `100` nas árvores revisadas quando `formRequisite` não é vazio |
| `formRequisite` | forma anterior/necessária | preserva a sequência própria de cada raça |
| `stackDrainMultiplier` | custo extra de formas acumuláveis | reduzido na linha Saiyajin para evitar quedas rápidas |
| `maxCostMultiplier` | custo máximo da forma | reduzido nas formas Saiyajin conforme o poder |
| `baselineFormDrain` | base global do dreno de formas | `55`, em vez de `80` no original |

## Stats de raça

`races/human/stats.json` e `races/frostdemon/stats.json` não foram modificados.
As imagens recebidas mostram exatamente a diferenciação que foi preservada:

- Humano: perfil versátil, com base `STR 2`, `PWR 2` e `ENE 5`.
- Freeza/Frost Demon: perfil de ki, com base `PWR 5`, `ENE 4` e crescimento de
  `PWR` de `1,0`.

Assim, o rebalanceamento melhora tempo de progressão e sustentabilidade das
formas sem apagar a identidade de cada raça.
