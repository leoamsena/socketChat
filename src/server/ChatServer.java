package src.server;

import java.io.BufferedReader;
import java.io.EOFException;
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
    private ObjectOutputStream saida;

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

    public void sendToAll(String mensagem) {
        try {
            for (ObjectOutputStream saida : clientes) {

                saida.writeObject(nome + ": " + mensagem);
                saida.flush();

            }
        } catch (Exception e) {
            System.out.println("ERRO AO ENVIAR MENSAGEM!" + e);
        }

    }

    public void run() {
        try {
            String mensagem;
            saida = new ObjectOutputStream(this.conexao.getOutputStream());
            clientes.add(saida);
            this.entrada = new ObjectInputStream(this.conexao.getInputStream());
            // saida.writeObject("QUAL SEU NOME?????");
            while (this.nome == null) {
                this.nome = (String) this.entrada.readObject();
            }

            saida.writeObject("Seja bem vindo " + nome + "!!!");
            sendToAll("entrou no chat!");
            while (true) {
                mensagem = (String) entrada.readObject();
                if (mensagem.equalsIgnoreCase("/sair")) {
                    break;
                } else {
                    try {
                        sendToAll(mensagem);
                        System.out.println("Enviando para todos: " + mensagem);
                    } catch (Exception e) {
                        System.out.println("ERRO!");
                        e.printStackTrace();

                    }
                }
            }

        } catch (EOFException e) {
            clientes.remove(saida);
            sendToAll("saiu do chat...");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}