package src;

import java.io.Serializable;

public class Mensagem implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String message;

    /*
     * codigos
     * 
     * 1 - mensagem simples
     * 
     * 2 - chamar atenção
     * 
     */

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }

    public Mensagem(String message) {
        this.message = message;
        this.code = 1; // mensagem de texto comum
    }

    public Mensagem(String message, int code) {
        this.message = message;
        this.code = code;
    }
}