# Gortic

Gortic é um projeto relacionado à disciplina de redes 1 com intuitos acadêmicos. Foi desenvolvido na linguagem Java e foram usados sockets para estabelecer a conexão entre cliente e servidor. Além disso, definimos a porta como: 1997. E por enquanto, o IP utilizado na aplicação, é localhost. 

## Descrição:
Gortic é um jogo baseado no jogo "gartic" onde no "gartic", as pessoas entram em uma sala e uma pessoa escolhida na rodada, desenha algo para que as outras pessoas que estão na sala, tentem adivinhar o que é aquele desenho. Na nossa aplicação, nós ao invés de desenhos, fizemos um jogo da forca. Temos uma lista com várias palavras e dicas na qual a aplicação pega alguma dessas palavras aleatoriamente e mostra para os clientes, como mostrado abaixo: ![alt text](https://media.discordapp.net/attachments/349715025226629134/446518893066977281/unknown.png?width=720&height=614)
Explicando melhor, assim que o cliente se conecta ao servidor, aparece para ele, a palavra atual e a dica da palavra: "Current Word" e "Current Word's clue". Além disso, o servidor mostrará as letras incorretas que outros clientes já tentaram. Após isso, o cliente "Tiago" arriscou a letra 'a' (na nossa aplicação, caso o tamanho da String seja maior que um, será considerada uma palavra) e o servidor mandou para ele e outros clientes que o cliente "Tiago" acertou uma letra da palavra e o score de "Tiago" aumentou no número de vezes que a letra aparece na palavra. Caso ele erre a letra, o servidor manda para todos os outros clientes a letra que ele errou, portanto, caso ele tente uma letra que já foi tentada, aparece apenas para "Tiago" que essa letra já foi tentada. Além disso, para evitar "Spam" nós implementamos "cooldown". Ou seja, quando um cliente tenta acertar alguma letra ou palavra, ele deve esperar cinco segundos para poder fazer uma nova tentativa (seja ela letra ou palavra). Caso o cliente acerte a palavra sugerida, a pontuação será calculada de acordo com o número de letras que ainda não foram descobertas.

## Instruções de uso:
O projeto foi feito no Eclipse (mais especificamente, um projeto Maven) mas como colocamos apenas as classes no repositório, basta importar o projeto.
Ao abrir o projeto, basta executar a classe Server e logo após, executar a classe Client. Cada classe Client executada, será pedido o "username" do cliente atual. Importante ressaltar que enquanto o usuário não colocar o username naquele cliente, não é estabelecido a conexão com o servidor.
## Funções utilizadas:
- classe ServerSocket
  - `accept()`
    - permite ao servidor aguardar pela conexão dos usuários
- classe Socket
  - Vale ressaltar que o construtor da classe Socket realiza a conexão com o servidor
  - `getOutputStream()`
    - utilizada para instanciar a classe DataOutputStream
  - `getInputStream()` 
    - utilizada para instanciar a classe BufferedReader DataOutputStream
- classe DataOutputStream 
  - `writeBytes(String arg0)`
    - utilizado para enviar dados do servidor para o cliente ou vice versa
- classe BufferedReader
  - `readLine()`
    - utilizado para receber informações vindas do servidor(no caso do cliente) ou do cliente(no caso do servidor)

Além disso, ocorrem dois tipos de concorrência, a concorrência local nos clientes que ocorre entre as threads de leitura e escrita, para que o jogo sempre atualize com o conteúdo vindo do servidor e não tenha que esperar um input para então atualizar, e a concorrência na rede que ocorre entre clientes, uma vez que os clientes podem fazer tentativas simultaneamente (respeitando o tempo de espera de 5 segundos por tentativa). Em relação à arquitetura do projeto usamos o padrão de projeto Mediator para servir os dados do jogo para todos os clientes "simultaneamente".
