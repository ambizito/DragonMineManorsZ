# DragonMineManorsZ 0.3.26.9.8

Nova versão de party e arenas para Minecraft 1.20.1, Forge 47.4.10 e Java 17.

- HUD no canto inferior esquerdo com nick, vida, líder, distância e estado.
  **Alt + P** abre o menu da party.
- Qualquer membro pode iniciar e contribuir para uma quest disponível.
  A conclusão e as recompensas são pessoais, compartilhadas apenas com os
  participantes elegíveis. Entrar na party não copia o histórico do líder.
- Batalhas que invocam inimigos transportam o grupo para uma cópia da arena do
  Cell em `dragonminez:quest_arena`, com terreno destrutível e retorno individual.
- Membros sem a quest desbloqueada podem acompanhar como apoio. Não recebem
  conclusão nem recompensa. Há recuperação após morte, desconexão ou reinício.
- Combate, balanceamento e requisitos de início das quests foram preservados.

JAR: `dragonminez-2.1.3-manorsz.0.3.26.9.8.jar`, igual nos clientes e servidor.
Não mantenha o JAR anterior junto do novo dentro da pasta `mods`.

**Validação:** compilação de produção, cinco testes de integração GameTest,
renderização real da HUD no cliente e inicialização de servidor dedicado com
89 mods. Veja [evidências e limites dos testes](verification/0.3.26.9.8/README.md).

Os arquivos integrais alterados estão em `source/dragonminez`, `defaultconfigs`,
`servedata` e nos arquivos de instalação. O patch de party/arena foi verificado
sobre os quatro patches anteriores. O perfil de desenvolvimento está instalado
em `D:\CurseForge\Instances\Dragon Mine Z`, com backup do JAR antigo.

Leia [como jogar e as regras de participação](party-arena.md) e
[como instalar](../README-CLIENTE.md).
