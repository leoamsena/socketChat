import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Flow;

import javax.swing.*;
import java.awt.*;

import ui.*;

public class ChatClient {
    public static void main(String[] args) {
        JFrame jframe = new JFrame("Chat!");
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLocationRelativeTo(null);

        jframe.setSize(800, 500);
        jframe.setLayout(new BorderLayout());

        jframe.add(new Login());

        jframe.setVisible(true);
    }
    /*
     * // ANTIGO MAIN public static void main(String[] args) { try { Socket cliente
     * = new Socket("127.0.0.1", 5000); ObjectInputStream entrada = new
     * ObjectInputStream(cliente.getInputStream()); ObjectOutputStream saida = new
     * ObjectOutputStream(cliente.getOutputStream()); String recebido = (String)
     * entrada.readObject(); System.out.println(recebido); String nome = "leo2";
     * saida.writeObject(nome); while (true) { recebido = (String)
     * entrada.readObject(); System.out.println(recebido); Scanner scn = new
     * Scanner(System.in); String input = scn.nextLine(); saida.writeObject(input);
     * }
     * 
     * } catch (Exception e) { System.out.println("Erro: " + e.getMessage()); } }
     */
}