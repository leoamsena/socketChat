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

/*
    Classe responsável pela interface de chat
 */
public class Chat extends JPanel {

    // painel que exibe as mensagems e texto digitado pelo usuário
    JTextPane jTxtArea;
    JTextField caixaDigitacao;

    // construtor da classe
    public Chat() {
        // define o layout da tela como layout de borda
        this.setLayout(new BorderLayout());
        JPanel jp;

        // cria um novo painel e define seu layout para um grid de 4 colunas
        jp = new JPanel();
        jp.setLayout(new GridLayout(1, 4));
        jp.setBackground(Color.WHITE);

        // tenta adicionar um icone na tela
        try {
            // busca caracter de separador de arquivos
            String sp = System.getProperty("file.separator");
            // define o caminho para as imagens
            String path = "asets" + sp + "img" + sp + "icon.png";

            // busca o stream do icone
            InputStream resource = ChatClient.class.getResourceAsStream(path);
            // instancia a classe do ícone usando o stream
            ImageIcon icon = new ImageIcon(ImageIO.read(resource));
            // recupera a imagem do ícone
            Image img = icon.getImage();
            // estiliza a imagem
            img = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
            // cria um novo ImageIcon para ser atrelado a um JLabel
            icon = new ImageIcon(img);
            // label que irá conter o icone
            JLabel icone = new JLabel();
            // coloca o ícone carregado no JLabel
            icone.setIcon(icon);
            // adiciona o label no JPanel
            jp.add(icone);
        // tratamento de exceção
        } catch (Exception e) {
            System.err.println("Erro ao adicionar icone! " + e.getMessage());

        }
        // define título e o adiciona na tela
        JLabel name = new JLabel("NSM!");
        jp.add(name);
        // define e mostra o botão de enviar alerta.
        JButton alerta = new JButton("Enviar alerta");
        jp.add(alerta);
        // controla o comportamento do botão de alerta.
        // escreve na caixa de digitação do usuário a base do 
        // "comando" de alerta
        alerta.addActionListener(e -> {
            this.caixaDigitacao.setText("/alerta [TEXTO]");
        });

        // define título do botão de atenção
        JButton atencao = new JButton("Chamar atenção");
        // controla comportamento do botão de atenção
        // chama o método do cliente que envia a mensagem 
        // de atenção para o server
        atencao.addActionListener(e -> {
            ChatClient.enviar("/atencao");
        });

        // adiciona o botão na tela
        jp.add(atencao);
        this.add(BorderLayout.NORTH, jp);

        // cria um text area para receber as mensagens do chat
        jTxtArea = new JTextPane();
        jTxtArea.setBackground(new Color(247, 247, 247));
        jTxtArea.setEditable(false);

        // adiciona esse text area em um painel com scroll
        JScrollPane chatBox = new JScrollPane(jTxtArea);
        // centraliza o painel com scroll
        this.add(BorderLayout.CENTER, chatBox);

        // cria um painel para a caixa de texto
        jp = new JPanel();
        jp.setBackground(new Color(232, 232, 232));

        // estilização da caixa de texto do usuário
        caixaDigitacao = new JTextField();
        caixaDigitacao.setFont(new Font("Serif", Font.PLAIN, 17));
        caixaDigitacao.setColumns(40);
        caixaDigitacao.setText("ESCREVA AQUI");
        caixaDigitacao.addActionListener(e -> enviarMensagem(caixaDigitacao.getText()));
        jp.add(caixaDigitacao);

        // cria o botão de enviar mensagem
        JButton jbEnviar = new JButton("Enviar");
        // configura o botão chamar o método de enviar mensagem
        jbEnviar.addActionListener(e -> enviarMensagem(caixaDigitacao.getText()));
        jp.add(jbEnviar);
        this.add(BorderLayout.SOUTH, jp);

    }

    /*
        Método que envia a mensagem para classe ChatClient
     */
    private void enviarMensagem(String mensagem) {
        // se a mensagem n for vazia
        if (!mensagem.trim().equals("")) {
            // limpa a caixa de texto
            this.caixaDigitacao.setText("");
            // e envia a mensagem
            ChatClient.enviar(mensagem);
        }
    }

    /*
        Método para adicionar uma nova mensagem ao chat
     */
    public void adicionarChat(Mensagem msg) {

        // tratamento para os códigos de som
        if (msg.getCode() == 5)
            tocarSom(2);
        else if (msg.getCode() == 1)
            tocarSom(1);

        // pega mensagem de texto
        String novaMensagem = msg.getMessage();

        try {
            // inicia processo de estilização da mensagems
            StyleContext sc = StyleContext.getDefaultStyleContext();
            Color cores[] = new Color[6];
            // cada mensagem tem uma cor
            cores[1] = Color.BLACK;
            cores[2] = Color.RED;
            cores[4] = new Color(46, 171, 0);
            cores[5] = new Color(46, 171, 0);

            String nome = msg.getNome();

            // atribui fontes e cores
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,
                    cores[msg.getCode()]);

            aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");

            StyledDocument doc = jTxtArea.getStyledDocument();
            int tam = doc.getLength();
            // monta a mensagem que será exibida
            String strAux = nome + ": " + novaMensagem + "\n";
            doc.insertString(tam, strAux, aset);

            // se a mensagem for do servidor
            if (msg.getCode() != 1) {
                // centraliza a mensagem
                SimpleAttributeSet center = new SimpleAttributeSet();
                StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
                doc.setParagraphAttributes(tam, tam + (strAux.length()), center, false);
            } else {
                // caso contrário, o conteúdo é alinhado à esquerda
                SimpleAttributeSet left = new SimpleAttributeSet();
                StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
                doc.setParagraphAttributes(tam, tam + (strAux.length()), left, false);
            }

        } catch (BadLocationException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
        Método que toca som
     */
    public void tocarSom(int codigo) {
        // 0 - atencao 1- nova mensagem 2 - online 3 - alerta
        try {
            String sons[] = new String[4];
            // tratando código errado
            if (codigo < 0 || codigo >= sons.length)
                throw new Exception("Música inexistente!");
            // cada som tem um arquivo
            sons[0] = "atencao.wav";
            sons[1] = "mensagem.wav";
            sons[2] = "online.wav";
            sons[3] = "alerta.wav";
            // recupera o separador de arquivos dos sistema e define um caminho para o audio
            String sp = System.getProperty("file.separator");
            String path = "asets" + sp + "sounds" + sp;
            path += sons[codigo];

            // recupera o audio como recurso
            InputStream resource = ChatClient.class.getResourceAsStream(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(resource);

            // player de audio
            Clip clip = AudioSystem.getClip();
            // carrega o audio no player
            clip.open(audioIn);
            // play
            clip.start();
        // tratamento de exceção
        } catch (IOException e) {
            System.err.println("Erro ao tocar audio! Caminho incorreto!");
        } catch (LineUnavailableException e) {
            System.err.println("Erro ao tocar audio! LineUnavailable!" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao tocar audio! Desconhecido!" + e.getMessage());

        }
    }

    /*
        Método que mostra a janela de alerta
     */
    public void gerarAlerta(Mensagem msg) {
        // toca o som de alerta
        tocarSom(3);
        // busca o texto da mensagem e o exibe em forma de modal de iformação
        String mensagem = msg.getMessage();
        String titulo = msg.getNome() + " disse:";
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /*
        Método que cria o efeito de "tremer a tela"
     */
    public void chamarAtencao() {
        // toca o som de atenção
        tocarSom(0);
        // acessa o componente pai da janela atual
        JFrame jf = (JFrame) SwingUtilities.getWindowAncestor(this);
        // recupera os pontos de localização da janela
        Point p = jf.getLocation();

        Timer timer;
        
        // define um timer com ação a cada 200ms
        timer = new Timer(200, new ActionListener() {
            int cont = 0;

            // define a ação do timer
            @Override
            public void actionPerformed(ActionEvent e) {
                // modifica a posição da janela, variando horizontalmente em 20
                jf.setLocation((int) p.getX() + 20, (int) p.getY());

                // define um novo timer com ação a cada 400ms
                Timer tm = new Timer(400, new ActionListener() {
                    int cont = 0;

                    //define a ação do timer
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // modifica a posição da janela, variando horizontalmente em -20
                        jf.setLocation((int) p.getX() - 20, (int) p.getY());
                        cont++;
                        // define repetições do movimento para criar o efeito de tremer
                        if (cont > 5) {
                            jf.setLocation(p);
                            ((Timer) e.getSource()).stop();
                        }
                    }
                });
                tm.restart();
                cont++;
                // define repetições do movimento para criar o efeito de tremer
                if (cont > 10) {
                    jf.setLocation(p);
                    ((Timer) e.getSource()).stop();

                }
            }
        });
        timer.restart();
    }
}