package src.client;

import java.io.*;

import src.client.ui.Chat;

public class ClientThread extends Thread {
    private ObjectInputStream entrada;
    private Chat c;

    public ClientThread(ObjectInputStream entrada, Chat c) throws IOException {
        this.entrada = entrada;
        this.c = c;
    }

    public void run() {
        try {
            String recebido;
            while (true) {
                System.out.println("LAÇO");
                recebido = (String) entrada.readObject();
                System.out.println("RODANDO!");
                System.out.println("ADICIONANDO!!!");
                c.adicionarChat(recebido);
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}