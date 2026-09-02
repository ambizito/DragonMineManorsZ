# DragonMineManorsZ

Repositório de configuração e balanceamento para **DragonMineZ 2.1.3**.

O objetivo é reduzir grind excessivo sem remover progressão: formas continuam
exigindo domínio da etapa anterior, mas o ganho de maestria e a duração das
transformações foram ajustados para um grupo pequeno de amigos.

## Conteúdo

- `config/dragonminez/`: configuração final para instalar em `config/dragonminez`.
- `patches/DragonMineZ-2.1.3-global-mastery.patch`: código-fonte da Maestria
  Global Saiyajin. A alteração deve ser compilada no JAR do DragonMineZ 2.1.3.
- `docs/`: especificação dos campos, justificativas e comparação com o original.

## Instalação

1. Faça cópia de segurança do mundo e da pasta `config/dragonminez`.
2. Copie o conteúdo de `config/dragonminez/` para a pasta equivalente do
   modpack, substituindo os arquivos.
3. Instale o JAR do DragonMineZ compilado com o patch em **servidor e clientes**.
   Não deixe dois JARs do DragonMineZ na pasta `mods`.

O patch é para a versão **2.1.3**. Configurações e patch foram pensados como um
conjunto; somente copiar os JSONs não habilita a Maestria Global Saiyajin.

## Princípios de balanceamento

- Nas árvores revisadas, cada transformação com pré-requisito exige `100` de
  maestria na anterior.
- Maestria passiva foi elevada de forma moderada; não há valores que completem
  uma forma em poucos minutos.
- Ganho por golpe das linhas retrabalhadas é `0,08` por golpe dado e recebido.
- Drains de energia foram reduzidos, mas formas mais fortes continuam custando
  mais energia.
- Stats-base de raça foram preservados. Humano continua versátil e Freeza
  continua especializado em poder/energia, como definido pelo mod original.

Leia primeiro [Alterações do original](docs/alteracoes-do-original.md) e
[Avaliação do V6](docs/avaliacao-v6.md).
