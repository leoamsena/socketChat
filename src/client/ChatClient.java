package src.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;
import java.awt.*;

import src.Mensagem;

import src.client.ui.*;

public class ChatClient {
    private static Socket cliente;
    private static ObjectInputStream entrada;
    private static ObjectOutputStream saida;
    private static JFrame jframe;

    public static void main(String[] args) {
        jframe = new JFrame("Chat!");
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLocationRelativeTo(null);

        jframe.setSize(800, 500);
        jframe.setLayout(new BorderLayout());

        jframe.getContentPane().add(new Login());

        jframe.setVisible(true);
    }

    public static void logar(String host, String porta, String nome) {
        try {
            cliente = new Socket(host, Integer.parseInt(porta));
            entrada = new ObjectInputStream(cliente.getInputStream());
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(nome);
            Mensagem msg = (Mensagem) entrada.readObject();
            String recebido = msg.getMessage();

            jframe.getContentPane().removeAll();
            jframe.revalidate();
            jframe.repaint();
            Chat chat = new Chat();
            jframe.getContentPane().add(chat);
            ClientThread clientThread = new ClientThread(entrada, chat);
            clientThread.start();
            jframe.revalidate();
            jframe.repaint();
            JOptionPane.showMessageDialog(jframe, recebido, "Mensagem do servidor!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(jframe, e.getMessage(), "ERRO!", JOptionPane.ERROR_MESSAGE);
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void enviar(String texto) {
        try {
            saida.writeObject(texto);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(jframe, e.getMessage(), "ERRO!", JOptionPane.ERROR_MESSAGE);
        }
    }
}