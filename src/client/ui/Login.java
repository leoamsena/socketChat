package src.client.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

import src.client.*;

/*
    Classe responsável pela interface de login
 */
public class Login extends JPanel {
    // campos de texto para host, porta e nome
    private JTextField host;
    private JTextField porta;
    private JTextField nome;

    // contrutor da classe
    public Login() {
        // define o layout como box
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // define um alinhamento central dos componentes
        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        // cria um novo painel para o título
        JPanel auxPanel = new JPanel();

        // cria uma label centralizado
        JLabel jl = new JLabel("NSM!", SwingConstants.CENTER);
        // define a fonte do texto
        jl.setFont(new Font("Serif", Font.PLAIN, 50));

        // adiciona o título no painel
        auxPanel.add(jl);
        this.add(auxPanel);


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
            img = img.getScaledInstance(250, 250, java.awt.Image.SCALE_SMOOTH);
            // cria um novo ImageIcon para ser atrelado a um JLabel
            icon = new ImageIcon(img);
            // label que irá conter o icone
            JLabel icone = new JLabel();
            // coloca o ícone carregado no JLabel
            icone.setIcon(icon);
            // cria um novo painel com o label de ícone e o adiciona na tela
            auxPanel = new JPanel();
            auxPanel.add(icone);
            this.add(auxPanel);
        // tratamento de exceção
        } catch (Exception e) {
            System.err.println("Erro ao adicionar icone! " + e.getMessage());
        }
        // cria um painel, adiciona um label a ele e coloca na tela
        auxPanel = new JPanel();
        jl = new JLabel("New Service of Messages (NSM) é um chat utilizando sockets!");
        auxPanel.add(jl);
        this.add(auxPanel);
        
        // cria os campos de texto do formulário de login
        this.host = new JTextField();
        this.host.setColumns(50);
        this.host.setText("Host");
        this.nome = new JTextField();
        this.nome.setColumns(55);
        this.nome.setText("Seu nome");
        this.porta = new JTextField();
        this.porta.setColumns(4);
        this.porta.setText("Porta");


        // define dois painéis para a estilização das caixas de texto.
        // para que tudo fique centralizado, cada caixa de texto vai em um
        // JPanel e seu tamanho é definido com uma altura e largura maximas
        JPanel firstLine = new JPanel();

        JPanel secondLine = new JPanel();

        firstLine.add(this.host);
        firstLine.add(this.porta);

        firstLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        this.add(firstLine);

        secondLine.add(this.nome);
        secondLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        this.add(secondLine);

        // cria um painel para receber o botão de entrar
        auxPanel = new JPanel();
        JButton jb = new JButton("Entrar!");
        // estilização do botão
        jb.setFont(new Font("Serif", Font.PLAIN, 50));
        // adiciona o evento de clique no botão
        jb.addActionListener(e -> ChatClient.logar(this.host.getText(), this.porta.getText(), this.nome.getText()));
        // adiciona o botão a tela
        auxPanel.add(jb);
        this.add(auxPanel);
    }
}