package src.client;

import java.io.*;

import src.Mensagem;
import src.client.ui.Chat;

/*
    Classe responsável por receber comunicação do servidor. 
    Extende a classe Thread, que controla a execução do método run.
 */
public class ClientThread extends Thread {
    private ObjectInputStream entrada;
    // interface do chat
    private Chat c;

    // construtor que recebe o caminho de entrada e a tela
    public ClientThread(ObjectInputStream entrada, Chat c) throws IOException {
        this.entrada = entrada;
        this.c = c;
    }

    // método que será executado na Thread
    public void run() {
        try {
            // mensagem recebida
            String recebido;
            // código da mensagem (lista de códigos na classe Mensagem)
            int code;
            // loop de interação do usuário. Ao se desconectar, irá lançar uma EOFException.
            while (true) {
                // Lê a mensagem vindo do server
                Mensagem msg = (Mensagem) entrada.readObject();
                // Obtêm o código 
                code = msg.getCode();
                // mensagem recebida em forma de texto
                recebido = msg.getMessage();

                // se a mensagem for do tipo atenção
                if (code == 2) {
                    // coloca a mensagem no chat
                    c.adicionarChat(msg);
                    // aciona o efeito na tela
                    c.chamarAtencao();
                    // se a mensagem for do tipo alerta
                } else if (code == 3) {
                    // aciona o alerta na tela
                    c.gerarAlerta(msg);
                // se for uma mensagem normal
                } else {
                    // apenas adiciona a mensagem
                    c.adicionarChat(msg);
                }
            }
        // tratamento de exceção
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}