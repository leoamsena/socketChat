package src.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public void chamarAtencao() {
        JFrame jf = (JFrame) SwingUtilities.getWindowAncestor(this);
        Point p = jf.getLocation();

        Timer timer;

        timer = new Timer(200, new ActionListener() {
            int cont = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                jf.setLocation((int) p.getX() + 20, (int) p.getY());

                Timer tm = new Timer(400, new ActionListener() {
                    int cont = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        jf.setLocation((int) p.getX() - 20, (int) p.getY());
                        cont++;
                        if (cont > 5) {
                            ((Timer) e.getSource()).stop();
                        }
                    }
                });
                tm.restart();
                cont++;
                if (cont > 10) {
                    ((Timer) e.getSource()).stop();

                }
            }
        });
        timer.restart();

        // JOptionPane.showMessageDialog(this, "Atenção!!!!", "Alguem chamou atenção!",
        // JOptionPane.WARNING_MESSAGE);
    }
}