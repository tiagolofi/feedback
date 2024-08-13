package br.gov.ma.feedback.mensageria;

public enum Mensagens {
    
    ADICIONADO(new Mensagem("Adicionado com sucesso!")),
    AUTORIZADO(new Mensagem("Acesso autorizado")),
    TOKEN_EXPIRADO(new Mensagem("Token expirado!")),
    NAO_AUTORIZADO(new Mensagem("Acesso não autorizado")),
    CPF_NAO_ENCONTRADO(new Mensagem("CPF não encontrado")),
    DELETADO(new Mensagem("As informações foram apagadas")),
    ATUALIZADO(new Mensagem("As informações foram atualizadas")),
    CPF_DESTINO_REMETENTE(new Mensagem("CPF Destino é igual ao do Remetente"));

    private final Mensagem mensagem;

    private Mensagens (Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

}
