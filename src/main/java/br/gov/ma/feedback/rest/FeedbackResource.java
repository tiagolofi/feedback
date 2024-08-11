package br.gov.ma.feedback.rest;

import java.util.List;

import br.gov.ma.feedback.modelos.Mensagens;
import br.gov.ma.feedback.modelos.Mensageria;
import br.gov.ma.feedback.mongo.Feedback;
import br.gov.ma.feedback.mongo.Usuario;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/feedback")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackResource {
    
    @POST
    @Path("/novo")
    public Response novoFeedback(Feedback feedback) {

        if (feedback.cpfDestino.equals(feedback.cpfRemetente)) {
            return Response.status(400)
                .entity(Mensagens.CPF_DESTINO_REMETENTE.getMensagem())
                .build();
        }

        Usuario usuarioDestino = Usuario.findByCpf(feedback.cpfDestino);
        Usuario usuarioRemetente = Usuario.findByCpf(feedback.cpfRemetente);

        if (usuarioRemetente.carteira > 0) {

            usuarioDestino.creditar(feedback.pontuacao);
            usuarioRemetente.debitar(feedback.pontuacao);           
            
            usuarioDestino.update();
            usuarioRemetente.update();

            feedback.setDataHora();
            feedback.persist();
            
            return Response.status(201)
                .entity(Mensagens.ADICIONADO.getMensagem())
                .build();
        }

        Mensageria mensagem = new Mensageria(String.format("Remetente %s sem saldo na carteira", usuarioRemetente.nome));

        return Response.status(400)
            .entity(mensagem)
            .build();
    }

    @GET
    @Path("/comentarios-destino/{cpf}")
    public List<Feedback> retornaComentariosDestino(String cpf) {
        return Feedback.listarComentariosByCpfDestino(cpf);
    }

    @GET
    @Path("/comentarios-remetente/{cpf}")
    public List<Feedback> retornaComentariosRementente(String cpf) {
        return Feedback.listarComentariosByCpfRemetente(cpf);
    }

    @GET
    @Path("/comentario/{id}")
    public Feedback retornaComentarioById(String id) {
        return Feedback.listarComentarioById(id);
    }

}
