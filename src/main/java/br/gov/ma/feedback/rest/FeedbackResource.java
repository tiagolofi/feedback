package br.gov.ma.feedback.rest;

import java.util.List;

import br.gov.ma.feedback.mensageria.Mensagens;
import br.gov.ma.feedback.modelos.FeedbackEditarComentario;
import br.gov.ma.feedback.mensageria.Mensagem;
import br.gov.ma.feedback.mongo.Carteira;
import br.gov.ma.feedback.mongo.Feedback;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/feedback")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackResource {
    
    @POST
    @Path("/novo")
    @RolesAllowed("user")
    @Timed(value = "feedback", extraTags = {"assunto", "negocio", "categoria", "cliente"}, percentiles = {0.95, 0.99})
    public Response novoFeedback(Feedback feedback) {

        if (feedback.cpfDestino.equals(feedback.cpfRemetente)) {
            return Response.status(400)
                .entity(Mensagens.CPF_DESTINO_REMETENTE.getMensagem())
                .build();
        }

        Carteira carteiraDestino = Carteira.findByCpf(feedback.cpfDestino);
        Carteira carteiraRemetente = Carteira.findByCpf(feedback.cpfRemetente);

        if (carteiraRemetente.saldoCarteira > 0) {

            carteiraDestino.creditarPontuacao(feedback.pontuacao);
            carteiraRemetente.debitarSaldo(feedback.pontuacao);

            feedback.setDataHora();
            feedback.persist();
            
            return Response.status(201)
                .entity(Mensagens.ADICIONADO.getMensagem())
                .build();
        }

        Mensagem mensagem = new Mensagem(String.format("Remetente %s não possui saldo disponível!", feedback.cpfRemetente));

        return Response.status(400)
            .entity(mensagem)
            .build();
    }

    @GET
    @Path("/comentario/{id}")
    @RolesAllowed("user")
    @Timed(value = "feedback", extraTags = {"assunto", "utilitario", "categoria", "developer"}, percentiles = {0.95, 0.99})
    public Feedback retornaComentarioById(String id) {
        return Feedback.listarComentarioById(id);
    }

    @GET
    @Path("/comentarios-destino/{cpf}")
    @RolesAllowed("user")
    @Timed(value = "feedback", extraTags = {"assunto", "utilitario", "categoria", "developer"}, percentiles = {0.95, 0.99})
    public List<Feedback> retornaComentariosDestino(String cpf) {
        return Feedback.listarComentariosByCpfDestino(cpf);
    }

    @GET
    @Path("/comentarios-remetente/{cpf}")
    @RolesAllowed("user")
    @Timed(value = "feedback", extraTags = {"assunto", "utilitario", "categoria", "developer"}, percentiles = {0.95, 0.99})
    public List<Feedback> retornaComentariosRementente(String cpf) {
        return Feedback.listarComentariosByCpfRemetente(cpf);
    }

    @PUT
    @Path("/editar")
    @RolesAllowed({"user", "moderator"})
    @Timed(value = "feedback", extraTags = {"assunto", "auditoria", "categoria", "cliente"}, percentiles = {0.95, 0.99})
    public Response editarFeedback(FeedbackEditarComentario feedbackCamposEditaveis) { 
        
        Feedback feedback = Feedback.listarComentarioById(feedbackCamposEditaveis.id);

        feedback.setComentario(feedbackCamposEditaveis.comentario);
        feedback.setTipoReconhecimento(feedbackCamposEditaveis.tipoReconhecimento);
        feedback.setDataHora();

        feedback.update();
        
        return Response.status(200)
            .entity(Mensagens.ATUALIZADO.getMensagem())
            .build();
    }

    @DELETE
    @Path("/remover/{id}")
    @RolesAllowed({"user", "moderator"})
    @Timed(value = "feedback", extraTags = {"assunto", "negocio", "categoria", "cliente"}, percentiles = {0.95, 0.99})
    public Response removerComentario(String id) {
        Feedback feedback = Feedback.listarComentarioById(id);

        Carteira carteiraQueDevolve = Carteira.findByCpf(feedback.cpfDestino);
        Carteira carteiraQuePagou = Carteira.findByCpf(feedback.cpfRemetente);

        carteiraQuePagou.creditarSaldo(feedback.pontuacao);
        carteiraQueDevolve.debitarPontuacao(feedback.pontuacao);

        feedback.delete();

        return Response.status(200)
            .entity(Mensagens.DELETADO.getMensagem())
            .build();
    }

}
