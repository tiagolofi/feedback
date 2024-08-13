package br.gov.ma.feedback.rest;

import br.gov.ma.feedback.mensageria.Mensagens;
import br.gov.ma.feedback.mongo.DadosUsuario;
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
    public Response informar(DadosUsuario dados) {
        dados.persist();
        return Response.status(201)
            .entity(Mensagens.ADICIONADO.getMensagem())
            .build();
    }

    @GET
    @Path("/consulta/{cpf}")
    @RolesAllowed("user")
    public DadosUsuario retornaUsuario(String cpf) {
        return DadosUsuario.findByCpf(cpf);
    }

    @PUT
    @Path("/editar") // editar o CPF irá perder o vínculo do usuário, resolva cadastrando um novo perfil ou reeditando
    @RolesAllowed("user")
    public Response atualizar(DadosUsuario dados) {
        dados.update();
        return Response.status(200)
            .entity(Mensagens.ATUALIZADO.getMensagem())
            .build();
    }

    @DELETE
    @Path("/remover")
    @RolesAllowed("admin")
    public Response remover(DadosUsuario dados) {
        dados.delete();
        return Response.status(200)
            .entity(Mensagens.DELETADO.getMensagem())
            .build();
    }

}
