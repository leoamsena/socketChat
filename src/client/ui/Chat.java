package src.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

import src.Mensagem;
import src.client.ChatClient;

public class Chat extends JPanel {
    JTextPane jTxtArea;
    JTextField caixaDigitacao;

    public Chat() {
        this.setLayout(new BorderLayout());
        jTxtArea = new JTextPane();

        jTxtArea.setEditable(false);

        JScrollPane chatBox = new JScrollPane(jTxtArea);
        this.add(BorderLayout.CENTER, chatBox);

        JPanel jp = new JPanel();

        caixaDigitacao = new JTextField();
        caixaDigitacao.setColumns(60);
        caixaDigitacao.setText("ESCREVA AQUI");
        caixaDigitacao.addActionListener(e -> enviarMensagem(caixaDigitacao.getText()));
        jp.add(caixaDigitacao);

        JButton jbEnviar = new JButton("Enviar");
        jbEnviar.addActionListener(e -> enviarMensagem(caixaDigitacao.getText()));
        jp.add(jbEnviar);
        this.add(BorderLayout.SOUTH, jp);

    }

    private void enviarMensagem(String mensagem) {
        this.caixaDigitacao.setText("");
        ChatClient.enviar(mensagem);
    }

    public void adicionarChat(Mensagem msg) {
        String novaMensagem = msg.getMessage();

        try {
            StyleContext sc = StyleContext.getDefaultStyleContext();
            Color cores[] = new Color[5];
            cores[1] = Color.BLACK;
            cores[2] = Color.RED;
            cores[4] = Color.GREEN;
            String nome = (msg.getCode() != 4) ? msg.getNome() + ": " : "";
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,
                    cores[msg.getCode()]);

            aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
            aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_CENTER);

            StyledDocument doc = jTxtArea.getStyledDocument();

            doc.insertString(doc.getLength(), nome + novaMensagem + "\n", aset);
        } catch (BadLocationException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void gerarAlerta(Mensagem msg) {
        String mensagem = msg.getMessage();
        String titulo = msg.getNome() + " disse:";
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
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
                            jf.setLocation(p);
                            ((Timer) e.getSource()).stop();
                        }
                    }
                });
                tm.restart();
                cont++;
                if (cont > 10) {
                    jf.setLocation(p);
                    ((Timer) e.getSource()).stop();

                }
            }
        });
        timer.restart();
    }
}