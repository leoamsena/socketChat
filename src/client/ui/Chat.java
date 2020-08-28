package src.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.*;

import javax.imageio.ImageIO;
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
        JPanel jp;

        jp = new JPanel();
        jp.setLayout(new GridLayout(1, 4));
        jp.setBackground(Color.WHITE);
        try {
            String sp = System.getProperty("file.separator");
            String path = "asets" + sp + "img" + sp + "icon.png";

            InputStream resource = ChatClient.class.getResourceAsStream(path);
            ImageIcon icon = new ImageIcon(ImageIO.read(resource));
            Image img = icon.getImage();
            img = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);

            JLabel icone = new JLabel();
            icone.setIcon(icon);
            jp.add(icone);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar icone! " + e.getMessage());

        }
        JLabel name = new JLabel("NSM!");
        jp.add(name);
        JButton alerta = new JButton("Enviar alerta");
        jp.add(alerta);
        alerta.addActionListener(e -> {
            this.caixaDigitacao.setText("/alerta [TEXTO]");
        });

        JButton atencao = new JButton("Chamar atenção");
        atencao.addActionListener(e -> {
            ChatClient.enviar("/atencao");
        });
        jp.add(atencao);
        this.add(BorderLayout.NORTH, jp);

        jTxtArea = new JTextPane();
        jTxtArea.setBackground(new Color(247, 247, 247));
        jTxtArea.setEditable(false);

        JScrollPane chatBox = new JScrollPane(jTxtArea);
        this.add(BorderLayout.CENTER, chatBox);

        jp = new JPanel();
        jp.setBackground(new Color(232, 232, 232));

        caixaDigitacao = new JTextField();
        caixaDigitacao.setFont(new Font("Serif", Font.PLAIN, 17));
        caixaDigitacao.setColumns(40);
        caixaDigitacao.setText("ESCREVA AQUI");
        caixaDigitacao.addActionListener(e -> enviarMensagem(caixaDigitacao.getText()));
        jp.add(caixaDigitacao);

        JButton jbEnviar = new JButton("Enviar");
        jbEnviar.addActionListener(e -> enviarMensagem(caixaDigitacao.getText()));
        jp.add(jbEnviar);
        this.add(BorderLayout.SOUTH, jp);

    }

    private void enviarMensagem(String mensagem) {
        if (!mensagem.trim().equals("")) {
            this.caixaDigitacao.setText("");
            ChatClient.enviar(mensagem);
        }
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
            cores[4] = new Color(46, 171, 0);
            cores[5] = new Color(46, 171, 0);
            String nome = msg.getNome();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,
                    cores[msg.getCode()]);

            aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");

            StyledDocument doc = jTxtArea.getStyledDocument();
            int tam = doc.getLength();
            String strAux = nome + ": " + novaMensagem + "\n";
            doc.insertString(tam, strAux, aset);

            if (msg.getCode() != 1) { // se mensagem do servidor centraliza
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(tam, tam + (strAux.length()), center, false);
            } else {
                SimpleAttributeSet left = new SimpleAttributeSet();
                StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
                doc.setParagraphAttributes(tam, tam + (strAux.length()), left, false);
            }

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
            InputStream resource = ChatClient.class.getResourceAsStream(path);

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(resource);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (IOException e) {
            System.err.println("Erro ao tocar audio! Caminho incorreto!");
        } catch (LineUnavailableException e) {
            System.err.println("Erro ao tocar audio! LineUnavailable!" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao tocar audio! Desconhecido!" + e.getMessage());

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