package src.server;

import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("Aguardando conexão...");
                Socket con = ChatServer.getServer().accept();
                System.out.println("Cliente conectado...");
                Thread t = new ChatServer(con);
                t.start();

            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}