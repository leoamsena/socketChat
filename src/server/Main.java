package src.server;

import java.net.Socket;

/*
    Classe principal, o programa começa aqui.
 */
public class Main {
    //Método principal, será o primeiro método a ser executado.
    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("Aguardando conexão...");
                //Busca a referência para o socket do server e aceita a conexão requisitada
                Socket con = ChatServer.getServer().accept();
                System.out.println("Cliente conectado...");
                // Abre uma thread para cada cliente
                Thread t = new ChatServer(con);
                // e a inicia
                t.start();

            }
        // Lidando com erros
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}