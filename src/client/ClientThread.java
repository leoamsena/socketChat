package src.client;

import java.io.*;

import src.Mensagem;
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
            int code;
            while (true) {
                Mensagem msg = (Mensagem) entrada.readObject();
                code = msg.getCode();
                recebido = msg.getMessage();

                if (code == 2) {
                    c.adicionarChat(msg);
                    c.chamarAtencao();
                } else if (code == 3) {
                    c.gerarAlerta(msg);
                } else {
                    c.adicionarChat(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}