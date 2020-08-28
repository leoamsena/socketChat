package src.client.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

import src.client.*;

public class Login extends JPanel {
    private JTextField host;
    private JTextField porta;
    private JTextField nome;

    public Login() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel auxPanel = new JPanel();

        JLabel jl = new JLabel("NSM!", SwingConstants.CENTER);
        jl.setFont(new Font("Serif", Font.PLAIN, 50));

        auxPanel.add(jl);
        this.add(auxPanel);

        try {
            String sp = System.getProperty("file.separator");
            String path = "asets" + sp + "img" + sp + "icon.png";

            InputStream resource = ChatClient.class.getResourceAsStream(path);
            ImageIcon icon = new ImageIcon(ImageIO.read(resource));
            Image img = icon.getImage();
            img = img.getScaledInstance(250, 250, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);

            JLabel icone = new JLabel();
            icone.setIcon(icon);
            auxPanel = new JPanel();
            auxPanel.add(icone);
            this.add(auxPanel);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar icone! " + e.getMessage());
        }
        auxPanel = new JPanel();
        jl = new JLabel("New Service of Messages (NSM) é um chat utilizando sockets!");
        auxPanel.add(jl);
        this.add(auxPanel);
        /*
         * JTextField teste = new JTextField(); teste.setColumns(20); this.add(teste);
         */
        this.host = new JTextField();
        this.host.setColumns(50);
        this.host.setText("Host");
        this.nome = new JTextField();
        this.nome.setColumns(55);
        this.nome.setText("Seu nome");
        this.porta = new JTextField();
        this.porta.setColumns(4);
        this.porta.setText("Porta");

        JPanel firstLine = new JPanel();

        JPanel secondLine = new JPanel();

        firstLine.add(this.host);
        firstLine.add(this.porta);

        firstLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        this.add(firstLine);

        secondLine.add(this.nome);
        secondLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        this.add(secondLine);

        auxPanel = new JPanel();
        JButton jb = new JButton("Entrar!");
        jb.setFont(new Font("Serif", Font.PLAIN, 50));
        jb.addActionListener(e -> ChatClient.logar(this.host.getText(), this.porta.getText(), this.nome.getText()));
        auxPanel.add(jb);
        this.add(auxPanel);
    }
}