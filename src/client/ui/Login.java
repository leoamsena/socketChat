package src.client.ui;

import javax.swing.*;
import java.awt.*;

import src.client.*;

public class Login extends JPanel {
    private JTextField host;
    private JTextField porta;
    private JTextField nome;

    public Login() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel jl = new JLabel("CHAT!", SwingConstants.CENTER);
        jl.setFont(new Font("Serif", Font.PLAIN, 50));
        this.add(jl);

        jl = new JLabel("Explicando os bagulhos doido do chat!");
        this.add(jl);
        /*
        JTextField teste = new JTextField();
        teste.setColumns(20);
        this.add(teste);
        */
        this.host = new JTextField();
        this.host.setColumns(50);
        this.nome = new JTextField();
        this.nome.setColumns(55);
        this.porta = new JTextField();
        this.porta.setColumns(4);

        JPanel firstLine = new JPanel();

        JPanel secondLine = new JPanel();

        firstLine.add(this.host);
        firstLine.add(this.porta);

        firstLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        this.add(firstLine);

        secondLine.add(this.nome);
        secondLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        this.add(secondLine);

        JButton jb = new JButton("Entrar!");
        jb.setFont(new Font("Serif", Font.PLAIN, 50));
        jb.addActionListener(e -> ChatClient.logar(this.host.getText(), this.porta.getText(), this.nome.getText()));
        this.add(jb);

    }
}