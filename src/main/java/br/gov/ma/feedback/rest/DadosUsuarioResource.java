package br.gov.ma.feedback.rest;

import br.gov.ma.feedback.mensageria.Mensagens;
import br.gov.ma.feedback.mongo.DadosUsuario;
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
@Path("/perfil")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DadosUsuarioResource {
    
    @POST
    @Path("/novo")
    @RolesAllowed("user")
    @Timed(value = "perfil", extraTags = {"path", "/novo", "assunto", "negocio", "categoria", "cliente"}, percentiles = {0.5, 0.95, 0.99})
    public Response informar(DadosUsuario dados) {
        dados.persist();
        return Response.status(201)
            .entity(Mensagens.ADICIONADO.getMensagem())
            .build();
    }

    @GET
    @Path("/consulta/{cpf}")
    @RolesAllowed("user")
    @Timed(value = "perfil", extraTags = {"path", "/consulta/{cpf}", "assunto", "utilitario", "categoria", "developer"}, percentiles = {0.5, 0.95, 0.99})
    public DadosUsuario retornaUsuario(String cpf) {
        return DadosUsuario.findByCpf(cpf);
    }

    @PUT
    @Path("/editar") // editar o CPF irá perder o vínculo do usuário, resolva cadastrando um novo perfil ou reeditando
    @RolesAllowed("user")
    @Timed(value = "perfil", extraTags = {"path", "/editar", "assunto", "negocio", "categoria", "cliente"}, percentiles = {0.5, 0.95, 0.99})
    public Response atualizar(DadosUsuario dados) {
        dados.update();
        return Response.status(200)
            .entity(Mensagens.ATUALIZADO.getMensagem())
            .build();
    }

    @DELETE
    @Path("/remover")
    @RolesAllowed("admin")
    @Timed(value = "perfil", extraTags = {"path", "/remover", "assunto", "negocio", "categoria", "cliente"}, percentiles = {0.5, 0.95, 0.99})
    public Response remover(DadosUsuario dados) {
        dados.delete();
        return Response.status(200)
            .entity(Mensagens.DELETADO.getMensagem())
            .build();
    }

}
