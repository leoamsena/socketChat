
# NSM!

NSM é um chat multi-usuário criado para a disciplina de Redes de Computadores da Universidade Federal de Lavras.

## Manual de instalação
Para compilar o projeto no Linux basta acessar o diretório raiz e executar o arquivo compile.sh  através do comando: 
```
./compile.sh
```

Caso seja de sua preferência é possível executar os comandos individualmente, sendo eles:
```
rm -f bin/
find -name "*.java" > sources.txt;
javac -d bin @sources.txt
cp -r src/client/asets/ bin/src/client/
rm sources.txt
```
Para compilação em Windows (não testada):

```
dir /s /B *.java > sources.txt
javac -d bin @sources.txt
cp -r src/client/asets/ bin/src/client/
```
## Executar após compilação

Para executar o lado servidor do projeto, após a sua compilação basta, estando na pasta raiz do projeto, digitar os comandos:
```
java -cp bin src.server.Main
```

Já para executar o lado cliente do projeto basta executar o seguinte comando:
```
java -cp bin src.client.ChatClient
```

## Hierarquia de arquivos
```
src
├── Mensagem.java   ->   Classe  'mensagem' que é enviada ao cliente pelo servidor (já o cliente envia Strings simples ao servidor)
├── client -> pacote com classes referentes ao lado do cliente da aplicação
│   ├── ChatClient.java -> Classe principal. Reponsável por iniciar a janela gráfica.
│   ├── ClientThread.java -> Classe responsável por receber mensagens do servidor 
│   ├── asets -> Imagens e Sons tocados pela aplicação
│   │   ├── img
│   │   │   └── icon.png
│   │   └── sounds
│   │       ├── alerta.wav
│   │       ├── atencao.wav
│   │       ├── mensagem.wav
│   │       └── online.wav
│   └── ui -> pacote que contem as classes de interface de usuário
│       ├── Chat.java -> Classe da tela de chat 
│       └── Login.java -> Classe da tela de "login"
└── server -> pacote com classes referentes ao lado do servidor da aplicação
    ├── ChatServer.java -> Classe que instancia o Socket e define os parametros
    └── Main.java -> Classe principal responsável por instanciar a classe "ChatServer.java"
```