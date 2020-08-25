package src.client.ui;

import javax.swing.*;
import java.awt.*;

import src.client.*;

public class Login extends JPanel {
    private JTextField host;
    private JTextField porta;
    private JTextField nome;

    public Login() {

        this.setLayout(new BorderLayout());
        JLabel jl = new JLabel("CHAT!", SwingConstants.CENTER);
        jl.setFont(new Font("Serif", Font.PLAIN, 50));
        this.add(BorderLayout.NORTH, jl);

        this.host = new JTextField();
        this.host.setColumns(50);
        this.nome = new JTextField();
        this.nome.setColumns(56);
        this.porta = new JTextField();
        this.porta.setColumns(4);

        JPanel jpCenter = new JPanel();

        jpCenter.setLayout(new BoxLayout(jpCenter, BoxLayout.Y_AXIS));

        JPanel firstLine = new JPanel();
        JPanel secondLine = new JPanel();

        firstLine.add(this.host);
        firstLine.add(this.porta);

        secondLine.add(this.nome);

        jpCenter.add(firstLine);

        jpCenter.add(secondLine);

        this.add(BorderLayout.CENTER, jpCenter);

        JButton jb = new JButton("Entrar!");
        jb.setFont(new Font("Serif", Font.PLAIN, 50));
        jb.addActionListener(e -> ChatClient.logar(this.host.getText(), this.porta.getText(), this.nome.getText()));
        this.add(BorderLayout.SOUTH, jb);

    }
}