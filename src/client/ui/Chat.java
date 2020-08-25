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
        jTxtArea.setText("testando\ntestando2\ntestando3");
        jTxtArea.setEditable(false);
        JScrollPane chatBox = new JScrollPane(jTxtArea);
        this.add(BorderLayout.NORTH, chatBox);

        caixaDigitacao = new JTextField();
        this.add(BorderLayout.CENTER, caixaDigitacao);

        JButton jbEnviar = new JButton("Enviar");
        jbEnviar.addActionListener(e -> ChatClient.enviar(caixaDigitacao.getText()));
        this.add(BorderLayout.EAST, jbEnviar);

    }

    public void adicionarChat(String novaMensagem) {
        jTxtArea.append("\n" + novaMensagem);
    }
}