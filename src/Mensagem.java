package src;

import java.io.Serializable;

/*
    Classe que define uma mensagem, extende Serializable para que possa ser transformada em texto para envio na rede.
*/
public class Mensagem implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
    private String nome;

    /*
     * codigos
     * 
     * 1 - mensagem simples
     * 
     * 2 - chamar atenção
     * 
     * 3 - Alerta
     * 
     * 4 - mensagem do servidor
     * 
     * 5 - novo usuario online
     * 
     */

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }

    public String getNome() {
        return this.nome;
    }

    public Mensagem(String message, String nome) {
        this.message = message;
        this.nome = nome;
        this.code = 1; // mensagem de texto comum
    }

    public Mensagem(String message, String nome, int code) {
        this.message = message;
        this.code = code;
        this.nome = nome;
    }
}