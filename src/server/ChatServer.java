package src.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import src.Mensagem;

/*
    Controla o servidor por onde as mensagems passam.
    Extende a classe Thread, que controla a execução do método run.
 */
public class ChatServer extends Thread {
    // public static int porta = 5000;
    // Lista de clientes conectados no chat
    private static ArrayList<ChatServer> clientes = new ArrayList<>();
    // Socket do servidor.
    private static ServerSocket server = null;
    // nome do usuário
    private String nome;
    // socket do usuário
    private Socket conexao;
    // entrada de dados do usuário
    private ObjectInputStream entrada;
    // saida de dados do servidro para o user
    private ObjectOutputStream saida;

    private ObjectOutputStream getSaida() {
        return this.saida;
    }

    private String getNome() {
        return this.nome;
    }

    // Construtor do server sem especificar uma conexão
    public static ServerSocket getServer() throws IOException {
        if (server == null) {
            // atribui um novo socket para o servidor ouvindo na porta 5000
            server = new ServerSocket(5000);
            System.out.println("Servidor iniciado na porta 5000!");
        }
        return server;
    }

    // Construtor do server especificando uma conexão
    public ChatServer(Socket conexao) throws IOException {
        this.conexao = conexao;

        if (server == null) {
            // atribui um novo socket para o servidor ouvindo na porta 5000
            server = new ServerSocket(5000);
            // inicializa a lista de clientes vazia
            clientes = new ArrayList<>();
            System.out.println("Servidor iniciado na porta 5000!");
        }
    }
    
    // Método que envia uma mensagem para todos os clientes conectados
    public void sendToAll(Mensagem msg) {
        try {
            // Lista de clientes que não estão mais com conexão ativa
            ArrayList<String> sairam = new ArrayList<>();
            // para cada cliente registrado no servidor
            for (ChatServer cliente : clientes) {
                // recuperando o caminho de saída do cliente atual
                ObjectOutputStream saida = cliente.getSaida();
                // tenta enviar a mensagem para o cliente atual
                try {
                    // escreve a mensagem na saída
                    saida.writeObject(msg);
                    // envia
                    saida.flush();
                    // em caso de erro, entende-se que o cliente não tem mais uma conexão ativa, 
                    // enviaremos uma mensagem do servidor informando.
                } catch (java.net.SocketException e) {
                    // remove o cliente desconectado da lista do server
                    clientes.remove(cliente);
                    // interrompe a conexão do cliente desconectado
                    cliente.interrupt();
                    // adiciona o cliente na lista de desconectados
                    sairam.add(cliente.getNome());
                }
            }
            // se existe algum cliente desconectado
            if (sairam.size() > 0) {
                // para cada um deles
                for (String saiu : sairam)
                    // chama recursivamente este método para informar a todos da saída do cliente
                    this.sendToAll(new Mensagem(saiu + " saiu do chat...", "Servidor", 4));
            }
        } catch (Exception e) {
            System.out.println("ERRO AO ENVIAR MENSAGEM!" + e);
        }

    }

    // método que será executado na Thread
    public void run() {
        try {
            String mensagem;
            // obtêm a saída da conexão atual
            saida = new ObjectOutputStream(this.conexao.getOutputStream());
            // insere o cliente atual na lista de clientes
            clientes.add(this);
            // obtêm a entrada da conexão atual
            this.entrada = new ObjectInputStream(this.conexao.getInputStream());

            // O servidor aguarda o usuário informar seu nome
            while (this.nome == null) {
                this.nome = (String) this.entrada.readObject();
            }

            // definindo mensagem boas vindas do servidor 
            Mensagem msg = new Mensagem("Seja bem vindo " + nome + "!!!", "Servidor");
            // enviando mensagem para o cliente
            saida.writeObject(msg);
            // informa a todos os usuários a nova conexão
            sendToAll(new Mensagem(this.nome + " entrou no chat!", "Servidor", 5));
            // loop de interação do usuário. Ao se desconectar, irá lançar uma EOFException.
            while (true) {
                // lê a mensagem do usuário
                mensagem = (String) entrada.readObject();
                try {
                    // se a mensagem for do tipo atenção
                    if (mensagem.equalsIgnoreCase("/atencao")) {
                        // envia o alerta para todos os usuários
                        sendToAll(new Mensagem("chamou atenção", this.nome, 2));
                        //se a mensagem for do tipo alerta
                    } else if (mensagem.split(" ")[0].equalsIgnoreCase("/alerta")) {
                        // obtêm o texto a ser exibido
                        int pos = mensagem.indexOf(" ");
                        String aux = mensagem.substring(pos + 1);
                        // envia a todo o alerta
                        sendToAll(new Mensagem(aux, this.nome, 3));
                        // caso seja uma mensagem normal
                    } else {
                        // envia a todos
                        sendToAll(new Mensagem(mensagem, this.nome, 1));
                        System.out.println("Enviando para todos: " + mensagem);
                    }
                } catch (Exception e) {
                    System.out.println("ERRO!");
                    e.printStackTrace();
                }
            }
            // caso o usuário seja desconectado 
        } catch (EOFException e) {
            // remove da lista de clientes
            clientes.remove(this);
            // comunica a todos a desconexão
            sendToAll(new Mensagem(this.nome + " saiu do chat...", "Servidor", 4));
            // fecha a conexão do usuário desconectado
            this.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}