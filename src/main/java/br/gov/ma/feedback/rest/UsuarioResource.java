package br.gov.ma.feedback.rest;

import br.gov.ma.feedback.mensageria.Mensagens;
import br.gov.ma.feedback.modelos.CredencialEditarAcesso;
import br.gov.ma.feedback.mensageria.Mensagem;

import br.gov.ma.feedback.mongo.Carteira;
import br.gov.ma.feedback.mongo.Credenciais;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @POST
    @Path("/novo")
    @PermitAll
    public Response criar(Credenciais credenciais) {
        if (!credenciais.verificaCpfDuplicado() && credenciais.validarCpf()){
            
            credenciais.setSenhaEncriptada();
            credenciais.persist();

            Carteira carteira = new Carteira();
            carteira.novaCarteira(credenciais.cpf);

            return Response.status(201)
                .entity(Mensagens.ADICIONADO.getMensagem())
                .build();
        }

        Mensagem mensagem = new Mensagem(String.format("Usuário já cadastrado ou CPF inválido: %s", credenciais.cpf));

        return Response.status(400)
            .entity(mensagem)
            .build();
    }

    @PUT
    @Path("/conceder-acessos")
    @RolesAllowed("admin")
    public Response atualizar(CredencialEditarAcesso credencialCamposEditaveis) {

        Credenciais credenciais = Credenciais.findByCpf(credencialCamposEditaveis.cpf);
        
        if (credenciais != null) {

            credenciais.setAcessos(credencialCamposEditaveis.acessos);
            credenciais.update();
    
            return Response.status(200)
                .entity(Mensagens.ATUALIZADO.getMensagem())
                .build();
        }

        return Response.status(400)
            .entity(Mensagens.CPF_NAO_ENCONTRADO.getMensagem())
            .build();
    }

    @DELETE
    @Path("/apagar/{cpf}")
    @RolesAllowed("admin")
    public Response remover(String cpf) {
        Credenciais.removerCpf(cpf);
        return Response.status(200)
            .entity(Mensagens.DELETADO.getMensagem())
            .build();
    }

}
