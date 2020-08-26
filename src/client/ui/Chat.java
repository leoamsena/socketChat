package src.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.awt.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
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
        if (msg.getCode() == 5)
            tocarSom(2);
        else if (msg.getCode() == 1)
            tocarSom(1);
        String novaMensagem = msg.getMessage();

        try {
            StyleContext sc = StyleContext.getDefaultStyleContext();
            Color cores[] = new Color[6];
            cores[1] = Color.BLACK;
            cores[2] = Color.RED;
            cores[4] = Color.CYAN;
            cores[5] = Color.GREEN;
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

    // 0 - atencao 1- nova mensagem 2 - online 3 - alerta
    public void tocarSom(int codigo) {
        try {
            String sons[] = new String[4];
            if (codigo < 0 || codigo >= sons.length)
                throw new Exception("Música inexistente!");

            sons[0] = "atencao.wav";
            sons[1] = "mensagem.wav";
            sons[2] = "online.wav";
            sons[3] = "alerta.wav";
            String sp = System.getProperty("file.separator");
            String path = "asets" + sp + "sounds" + sp;
            path += sons[codigo];
            URL url = ChatClient.class.getResource(path);

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (IOException e) {
            System.out.println("Erro ao tocar audio! Caminho incorreto!");
        } catch (LineUnavailableException e) {
            System.out.println("Erro ao tocar audio! LineUnavailable!" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao tocar audio! Desconhecido!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void gerarAlerta(Mensagem msg) {
        tocarSom(3);
        String mensagem = msg.getMessage();
        String titulo = msg.getNome() + " disse:";
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void chamarAtencao() {
        tocarSom(0);
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