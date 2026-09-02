# Maestria Global Saiyajin

A maestria global é calculada a partir das maestrias que o personagem já possui.
Ela não cria um novo campo no save: personagens e mundos existentes permanecem
compatíveis.

O multiplicador de ganho de maestria é:

`1 + 9 × (maestriaGlobal / 100)^1,5`

Ele começa em `1,00x` e chega a `10,00x` quando todas as formas ponderadas estão
dominadas. O bônus acelera somente a maestria; atributos, dano e defesa não são
alterados por ele.

## Pesos

| Linha | Peso |
| --- | ---: |
| Básicas/Z | 15% |
| GT | 20% |
| Lendárias | 20% |
| Divinas: Blue e Rosé | 37% |
| Beast | 8% |

SSJ4 presente nos grupos `supersaiyan` e `oozaru` é contado apenas uma vez. Se
os dois valores diferirem, o maior deles é usado.

Inclui SSJ1, Grades, SSJ Mastered, SSJ2, SSJ3, Oozaru, Golden Oozaru, SSJ4,
SSJ4 Full Power, SSJ4 Limit Breaker, Ikari, SSJ Hybrid, SSJ Full Power,
Legendary 3, Legendary 4, God, Blue, Blue Evolved, Blue Perfected, Rosé,
Rosé Evolved, Rosé 3, Rosé Full Power e Beast.

Use `/dmzmastery global` para consultar o valor atual e o multiplicador.
