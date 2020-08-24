import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        /*
         * try { ChatServer server = new ChatServer(5000); while (true) {
         * server.waitConection(); } } catch (Exception e) { System.out.println("ERRO! "
         * + e.getMessage()); }
         */
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