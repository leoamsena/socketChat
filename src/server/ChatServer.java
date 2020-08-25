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

    public void sendToAll(String mensagem, int code) {
        try {
            for (ObjectOutputStream saida : clientes) {

                saida.writeObject(new Mensagem(nome + ": " + mensagem, code));
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
            Mensagem msg = new Mensagem("Seja bem vindo " + nome + "!!!");
            saida.writeObject(msg);
            sendToAll("entrou no chat!", 1);
            while (true) {
                mensagem = (String) entrada.readObject();
                try {
                    if (mensagem.equalsIgnoreCase("/atencao")) {
                        sendToAll("CHAMOU ATENÇÃO!", 2);
                    } else {

                        sendToAll(mensagem, 1);
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
            sendToAll("saiu do chat...", 1);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}