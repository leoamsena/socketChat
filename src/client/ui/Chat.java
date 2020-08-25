package src.client.ui;

import java.awt.*;
import javax.swing.*;

import src.client.ChatClient;

public class Chat extends JPanel {
    JTextArea jTxtArea;
    JTextField caixaDigitacao;

    public Chat() {
        this.setLayout(new BorderLayout());
        jTxtArea = new JTextArea();

        jTxtArea.setEditable(false);
        JScrollPane chatBox = new JScrollPane(jTxtArea);
        this.add(BorderLayout.CENTER, chatBox);

        JPanel jp = new JPanel();

        caixaDigitacao = new JTextField();
        caixaDigitacao.setColumns(60);
        caixaDigitacao.setText("ESCREVA AQUI");
        jp.add(caixaDigitacao);

        JButton jbEnviar = new JButton("Enviar");
        jbEnviar.addActionListener(e -> ChatClient.enviar(caixaDigitacao.getText()));
        jp.add(jbEnviar);
        this.add(BorderLayout.SOUTH, jp);

    }

    public void adicionarChat(String novaMensagem) {
        jTxtArea.append("\n" + novaMensagem);
    }
}