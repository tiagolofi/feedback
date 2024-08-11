package br.gov.ma.feedback.modelos;

public class Mensageria {
    
    public String mensagem;
    public Object objetoSerial;

    public Mensageria(String mensagem, Object objetoSerial) {
        this.mensagem = mensagem;
        this.objetoSerial = objetoSerial;
    }

    public Mensageria(String mensagem) {
        this.mensagem = mensagem;
    }
}
