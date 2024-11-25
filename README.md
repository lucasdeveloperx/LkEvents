# Batata Quente

**Batata Quente** é um plugin para Minecraft onde os jogadores participam de um jogo divertido em que devem passar a batata quente para outro jogador antes que ela "exploda". O jogo é dinâmico e exige rapidez e estratégia para evitar ser o último com a batata.

## Comandos

| Comando                | Descrição                                                           | Permissão                    |
|------------------------|---------------------------------------------------------------------|------------------------------|
| `/batata entrar`        | Entra na fila do jogo de Batata Quente.                             | Nenhuma                      |
| `/batata sair`          | Sai da fila de Batata Quente.                                       | Nenhuma                      |
| `/batata começar`       | Começa o jogo de Batata Quente, caso haja jogadores suficientes.   | Nenhuma                      |
| `/batata setlobby`      | Define a localização do lobby do jogo.                              | `batataquente.setlobby`      |
| `/batata setarena`      | Define a localização da arena do jogo.                              | `batataquente.setarena`      |
| `/batata help`          | Mostra ajuda sobre os comandos do plugin Batata Quente.             | Nenhuma                      |

## Download

Você pode encontrar o plugin pronto para baixar [**aqui**](https://github.com/lucasdeveloperx/LkEvents/releases), ou se
você quiser, pode optar por clonar o repositório e dar build no plugin com suas alterações.

## Funcionalidades

### 1. **Jogo de Batata Quente**
- O jogo começa com os jogadores entrando na fila usando o comando `/batata entrar`.
- Quando o jogo é iniciado com o comando `/batata começar`, os jogadores são teleportados para a arena e o jogo começa.
- O jogador que estiver com a batata quente deve passá-la para outro jogador antes que a batata "exploda".
- O último jogador com a batata quente perde o jogo.

### 2. **Configuração do Lobby e Arena**
- O lobby e a arena são definidos pelos administradores usando os comandos `/batata setlobby` e `/batata setarena`.

### 3. **Sistema de Sons**
- O plugin toca sons de "XP" e "Pin" quando o jogador entra no jogo ou quando ele recebe a batata quente, criando uma experiência mais imersiva.

### 4. **Boss Bar**
- O tempo restante da rodada é mostrado para todos os jogadores na forma de uma **Boss Bar** colorida que diminui conforme o tempo passa.

## Requisitos
- Minecraft 1.16 ou superior
- Permissões configuradas no servidor para comandos específicos

## Como Instalar

1. Baixe o arquivo `.jar` do plugin.
2. Coloque o arquivo na pasta `plugins` do seu servidor Minecraft.
3. Reinicie o servidor ou use o comando `/reload` para carregar o plugin.
4. Defina o lobby e a arena com os comandos `/batata setlobby` e `/batata setarena`.

## Permissões

| Permissão                    | Descrição                                      |
|------------------------------|------------------------------------------------|
| `batataquente.setlobby`       | Permite definir a localização do lobby.        |
| `batataquente.setarena`       | Permite definir a localização da arena.        |

## Contribuições

Contribuições são bem-vindas! Para contribuir, basta fazer um fork deste repositório, criar uma branch com a sua modificação, e enviar um pull request com uma descrição detalhada da mudança.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

## Contato

Para mais informações ou ajuda, entre em contato com o desenvolvedor do plugin.

