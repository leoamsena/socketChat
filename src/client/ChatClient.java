package src.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

import src.Mensagem;

import src.client.ui.*;

/*
    Classe responsável pela tela do cliente
 */
public class ChatClient {
    // socket do cliente
    private static Socket cliente;
    // caminhos de entrada e saída do cliente
    private static ObjectInputStream entrada;
    private static ObjectOutputStream saida;
    // janela da tela
    private static JFrame jframe;

    // método executado 
    public static void main(String[] args) {
        // inicializa a janela com um título
        jframe = new JFrame("NSM!");
        // desabilita o redimensionamento
        jframe.setResizable(false);
        // faz o botão de fechar funcionar :)
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // posicionamento absoluto
        jframe.setLocationRelativeTo(null);
        // define o tamanho da janela
        jframe.setSize(800, 600);
        // adiciona a tela de login na janela
        jframe.getContentPane().add(new Login());
        // liga a visibilidade da janela
        jframe.setVisible(true);
    }

    // método com a lógica para inserir um novo cliente no chat
    public static void logar(String host, String porta, String nome) {
        try {
            // cria um novo socket e caminhos de entrada e saída para o novo usuário
            cliente = new Socket(host, Integer.parseInt(porta));
            entrada = new ObjectInputStream(cliente.getInputStream());
            saida = new ObjectOutputStream(cliente.getOutputStream());
            // informa ao servidor o nome do usuário
            saida.writeObject(nome);
            // busca a mensagem de boas vindas
            Mensagem msg = (Mensagem) entrada.readObject();
            String recebido = msg.getMessage();

            // remove todo o conteúdo da janela
            jframe.getContentPane().removeAll();
            // métodos para que o conteúdo da janela seja atualizado de forma visual
            jframe.revalidate();
            jframe.repaint();
            // adiciona a tela de chat na janela
            Chat chat = new Chat();
            jframe.getContentPane().add(chat);
            // inicializa a thread do cliente para ouvir o servidor
            ClientThread clientThread = new ClientThread(entrada, chat);
            clientThread.start();
            // métodos para que o conteúdo da janela seja atualizado de forma visual
            jframe.revalidate();
            jframe.repaint();
            // exibe para o usuário a mensagem de boas vindas
            JOptionPane.showMessageDialog(jframe, recebido, "Mensagem do servidor!", JOptionPane.INFORMATION_MESSAGE);

        // tratamento de exceção
        } catch (NumberFormatException e) {
            // mostra mensagem de erro de porta na forma de modal
            JOptionPane.showMessageDialog(jframe, "O campo 'Porta' deve ser um número!", "ERRO!",
                    JOptionPane.ERROR_MESSAGE);
        } catch (UnknownHostException e) {
            // mostra mensagem de erro de host na forma de modal
            JOptionPane.showMessageDialog(jframe, "O Host '" + host + "' não foi encontrado!", "ERRO!",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // mostra mensagem de erro genérica na forma de modal
            JOptionPane.showMessageDialog(jframe, e.getMessage(), "ERRO!", JOptionPane.ERROR_MESSAGE);
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // método que envia a mensagem do usuário para o server
    public static void enviar(String texto) {
        try {
            saida.writeObject(texto);
        // tratamento exceção
        } catch (Exception e) {
            // mostra mensagem de erro genérica na forma de modal
            JOptionPane.showMessageDialog(jframe, e.getMessage(), "ERRO!", JOptionPane.ERROR_MESSAGE);
        }
    }
}