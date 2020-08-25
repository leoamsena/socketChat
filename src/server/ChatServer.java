package src.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import src.Mensagem;

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

    public ChatServer(Socket conexao) throws IOException {
        this.conexao = conexao;

        if (server == null) {
            server = new ServerSocket(5000);
            clientes = new ArrayList<>();
            System.out.println("Servidor iniciado na porta 5000!");
        }
    }

    public void sendToAll(Mensagem msg) {
        try {
            for (ObjectOutputStream saida : clientes) {

                saida.writeObject(msg);
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

            while (this.nome == null) {
                this.nome = (String) this.entrada.readObject();
            }
            Mensagem msg = new Mensagem("Seja bem vindo " + nome + "!!!", "Servidor");
            saida.writeObject(msg);
            sendToAll(new Mensagem(this.nome + " entrou no chat!", "Servidor", 4));
            while (true) {
                mensagem = (String) entrada.readObject();
                try {
                    if (mensagem.equalsIgnoreCase("/atencao")) {
                        sendToAll(new Mensagem("chamou atenção", this.nome, 2));
                    } else if (mensagem.split(" ")[0].equalsIgnoreCase("/alerta")) {
                        int pos = mensagem.indexOf(" ");
                        String aux = mensagem.substring(pos + 1);
                        sendToAll(new Mensagem(aux, this.nome, 3));
                    } else {

                        sendToAll(new Mensagem(mensagem, this.nome, 1));
                        System.out.println("Enviando para todos: " + mensagem);

                    }
                } catch (Exception e) {
                    System.out.println("ERRO!");
                    e.printStackTrace();

                }
            }

        } catch (

        EOFException e) {
            clientes.remove(saida);
            sendToAll(new Mensagem("saiu do chat...", "Servidor", 4));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}