# DragonMineManorsZ

Repositório de configuração e balanceamento para **DragonMineZ 2.1.3**.

O objetivo é reduzir grind excessivo sem remover progressão: formas continuam
exigindo domínio da etapa anterior, mas o ganho de maestria e a duração das
transformações foram ajustados para um grupo pequeno de amigos.

## Conteúdo

- `config/dragonminez/`: configuração final para instalar em `config/dragonminez`.
- `patches/DragonMineZ-2.1.3-global-mastery.patch`: código-fonte da Maestria
  Global Saiyajin. A alteração deve ser compilada no JAR do DragonMineZ 2.1.3.
- `patches/DragonMineZ-2.1.3-kaioken-prerequisites.patch`: código-fonte completo
  dos pré-requisitos sequenciais do Kaioken e do aviso que informa qual forma
  anterior ainda precisa ser dominada.
- `docs/`: especificação dos campos, justificativas e comparação com o original.

Todos os arquivos modificados do DragonMineZ estão representados integralmente
nesses dois patches. Eles incluem `Character.java`, `SaiyanGlobalMastery.java`,
`MasteryCommand.java`, `DefaultFormsFactory.java`, `TransformationsHelper.java`,
`ClientStatsEvents.java` e os recursos de idioma alterados/adicionados. As
configurações finais do modpack ficam versionadas separadamente em `config/`.

Os patches devem ser aplicados ao repositório oficial
`https://github.com/DragonMineZ/dragonminez.git`, no commit da versão 2.1.3
`3c58ae869788a37fb42d0ed47a5494724a07735a`, nesta ordem:

```powershell
git apply DragonMineZ-2.1.3-global-mastery.patch
git apply DragonMineZ-2.1.3-kaioken-prerequisites.patch
```

## Instalação

1. Feche o Minecraft.
2. No CurseForge, abra **Opções do perfil** e ative **Permitir gerenciamento de
   conteúdo para este perfil** (`Allow Content Management for this profile`).
   Sem essa opção, o CurseForge pode restaurar o JAR original ao iniciar o jogo.
3. Faça uma cópia de segurança do mundo, da pasta `config/dragonminez` e do JAR
   original encontrado na pasta `mods` do perfil.
4. Copie o JAR compilado `dragonminez-2.1.3.jar` para a pasta `mods`, substituindo
   o arquivo original. Para a instância usada neste projeto, o destino é:

   ```text
   D:\CurseForge\Instances\Dragon Mine Z\mods\dragonminez-2.1.3.jar
   ```

5. Confira se existe apenas um JAR do DragonMineZ dentro de `mods`. O arquivo
   `.patch` deste repositório não deve ser colocado nessa pasta; ele serve para
   gerar o JAR durante a compilação.
6. Copie o conteúdo de `config/dragonminez/` para `config/dragonminez/` dentro do
   perfil, substituindo os arquivos existentes.
7. Inicie o modpack pelo CurseForge.

Em jogo multiplayer, instale o mesmo JAR e as mesmas configurações no
**servidor e em todos os clientes**.

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
