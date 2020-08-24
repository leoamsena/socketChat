import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer extends Thread {
    // public static int porta = 5000;
    private static ArrayList<ObjectOutputStream> clientes = new ArrayList<>();;
    private static ServerSocket server = null;
    private String nome;
    private Socket conexao;
    private ObjectInputStream entrada;

    public static ServerSocket getServer() throws IOException {
        if (server == null) {
            server = new ServerSocket(5000);
            System.out.println("Servidor iniciado na porta 5000!");
        }
        return server;
    }

    public void waitConection() throws IOException, ClassNotFoundException {
        System.out.println("Aguardando conexão na porta " + this.server.getLocalPort());
        Socket cliente = this.server.accept();
        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
        saida.flush();
        saida.writeObject("Qual é seu nome?");
        String nome;
        while ((nome = (String) entrada.readObject()) != "") {
            System.out.println("Cliente disse que o nome dele é: " + nome);
        }
        saida.writeObject("Seja bem-vindo " + nome + "!");
    }

    public ChatServer(Socket conexao) throws IOException {
        this.conexao = conexao;

        if (server == null) {
            server = new ServerSocket(5000);
            clientes = new ArrayList<>();
            System.out.println("Servidor iniciado na porta 5000!");
        }
    }

    public void sendToAll(ObjectOutputStream clienteEnviouSaida, String mensagem) throws IOException {

        for (ObjectOutputStream saida : clientes) {
            if (clienteEnviouSaida != saida) {
                saida.writeObject(nome + ": " + mensagem);
                saida.flush();
            }
        }
    }

    public void run() {
        try {
            String mensagem;
            ObjectOutputStream saida = new ObjectOutputStream(this.conexao.getOutputStream());
            clientes.add(saida);
            this.entrada = new ObjectInputStream(this.conexao.getInputStream());
            saida.writeObject("QUAL SEU NOME?????");
            while (this.nome == null) {
                this.nome = (String) this.entrada.readObject();
            }

            saida.writeObject("Seja bem vindo " + nome + "!!!");
            while ((mensagem = (String) entrada.readObject()) != null) {
                if (mensagem.equalsIgnoreCase("/sair")) {
                    break;
                } else {
                    sendToAll(saida, mensagem);
                    System.out.println("Enviando para todos: " + mensagem);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}