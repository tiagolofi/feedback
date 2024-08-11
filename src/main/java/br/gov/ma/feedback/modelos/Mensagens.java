package br.gov.ma.feedback.modelos;

public enum Mensagens {
    
    ADICIONADO(new Mensageria("Adicionado com sucesso!")),
    AUTORIZADO(new Mensageria("Acesso autorizado")),
    NAO_AUTORIZADO(new Mensageria("Acesso não autorizado")),
    DELETADO(new Mensageria("As informações foram apagadas")),
    ATUALIZADO(new Mensageria("As informações foram atualizadas")),
    CPF_DESTINO_REMETENTE(new Mensageria("CPF Destino é igual ao do Remetente"));

    private final Mensageria mensagem;

    private Mensagens (Mensageria mensagem) {
        this.mensagem = mensagem;
    }

    public Mensageria getMensagem() {
        return mensagem;
    }

}
