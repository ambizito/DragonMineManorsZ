# DragonMineManorsZ — pacote de servidor

Versão do modpack: **0.3.26.9.8**.

Pacote para **Minecraft 1.20.1**, **Forge 47.4.10** e **Java 17**.

Validado com um servidor dedicado real contendo 89 mods. O servidor chegou ao
estado `Done`, carregou as dimensões do modpack e iniciou o Simple Voice Chat.

## Instalação na EnxadaHost

1. Pare o servidor e faça backup de qualquer mundo existente.
2. No painel, instale o modloader **Forge** para Minecraft `1.20.1`.
3. Em versão do Forge, informe somente `47.4.10`.
4. Inicie uma vez, aguarde a criação dos arquivos e pare o servidor.
5. Envie o conteúdo desta pasta para a raiz do servidor via SFTP. As pastas
   `mods`, `config`, `defaultconfigs`, `dragonballs` e `world` devem ficar
   diretamente na raiz, e não dentro de outra pasta `servedata`. Confirme a
   substituição dos arquivos quando o painel perguntar.
6. Aceite a EULA pelo painel (ou altere `eula=false` para `eula=true` somente
   depois de ler e aceitar os termos da Mojang).
7. Inicie o servidor e acompanhe o console até aparecer `Done`.

Guias oficiais: [instalar Forge](https://ajuda.enxadahost.com/hc/pt-br/articles/51730845522067-Como-instalar-o-Forge)
e [instalar mods](https://ajuda.enxadahost.com/hc/pt-br/articles/51730845537299-Passo-a-Passo-Veja-como-instalar-seus-mods-favoritos).

Use Java 17 e, para este conjunto de mods, comece com pelo menos 10 GB de RAM.
Não defina `server-ip`: a hospedagem cuida do endereço de escuta.

O pacote assume `level-name=world`. Se o mundo da hospedagem tiver outro nome,
mova os dois arquivos de `world/serverconfig` para
`<nome-do-mundo>/serverconfig` antes de iniciar. Os arquivos em `defaultconfigs`
garantem as mesmas regras para mundos criados depois.

## Proteção, borda, dimensões e teleporte

Já estão instalados e configurados:

- **FTB Chunks/Teams**: proteção de terrenos e chunks carregados;
- **FTB Essentials**: `/home`, `/back`, `/spawn`, `/tpa`, `/rtp`, `/warp` e
  `/tpx` para administradores;
- **Waystones**: teleporte entre dimensões, com Waystones naturais
  inquebráveis;
- proteção vanilla de 16 blocos ao redor do spawn.

Depois de iniciar, dê OP ao administrador pelo console com `op NomeDoJogador`.
As instruções para criar áreas indestrutíveis, definir a borda do mapa, criar
warps, abrir dimensões e configurar whitelist estão em
[`ADMINISTRACAO-SERVIDOR.md`](ADMINISTRACAO-SERVIDOR.md).

## Simple Voice Chat

O mod usa UDP `24454`, configurado em
`config/voicechat/voicechat-server.properties`. Solicite/adicione essa porta UDP
no painel da hospedagem. Se a EnxadaHost fornecer outra porta adicional, altere
o campo `port` do arquivo para a porta fornecida.

## Compatibilidade dos clientes

Os jogadores precisam usar a mesma versão do modpack e o mesmo
`dragonminez-2.1.3-manorsz.0.3.26.9.8.jar` modificado presente em `mods`. Os mods listados em
`excluded-client-mods.txt` continuam somente no cliente e não devem ser
adicionados ao servidor.

`server-manifest.json` registra tamanho e SHA-256 de cada JAR para conferir se o
upload terminou sem corrupção.

O JAR modificado do DragonMineZ incluído neste pacote tem SHA-256
`3402661235C516DB1404326F0D992EDFFA743E246D8DEAE48DEB3D786E3CB0A7`.
