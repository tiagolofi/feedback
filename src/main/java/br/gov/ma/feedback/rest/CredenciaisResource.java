package br.gov.ma.feedback.rest;

import br.gov.ma.feedback.mensageria.Mensagens;
import br.gov.ma.feedback.modelos.CredencialEditarAcesso;
import br.gov.ma.feedback.mensageria.Mensagem;

import br.gov.ma.feedback.mongo.Carteira;
import br.gov.ma.feedback.mongo.Credenciais;
import br.gov.ma.feedback.mongo.DadosUsuario;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.security.PermitAll;
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
@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CredenciaisResource {

    @POST
    @Path("/novo")
    @PermitAll
    @Timed(value = "credencial", extraTags = {"assunto", "autenticacao", "categoria", "seguranca"}, percentiles = {0.95, 0.99})
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
    @Path("/troca-senha")
    @RolesAllowed("admin")
    @Timed(value = "credencial", extraTags = {"assunto", "negocio", "categoria", "seguranca"}, percentiles = {0.95, 0.99})
    public Response trocaSenha(Credenciais credenciais) {
        Credenciais antigas = Credenciais.findByCpf(credenciais.cpf);
        if (antigas != null) {
            credenciais.id = antigas.id;
            credenciais.setSenhaEncriptada();
            credenciais.update();
            return Response.status(200)
                .entity(Mensagens.ATUALIZADO.getMensagem())
                .build();
        }
        return Response.status(404)
            .entity(Mensagens.CPF_NAO_ENCONTRADO.getMensagem())
            .build();
    }

    @GET
    @Path("/mostrar-acessos/{cpf}")
    @RolesAllowed("user")
    @Timed(value = "credencial", extraTags = {"assunto", "negocio", "categoria", "seguranca"}, percentiles = {0.95, 0.99})
    public Response retornaAcessos(String cpf){

        Credenciais credenciais = Credenciais.findByCpf(cpf);

        if (credenciais != null) {
            CredencialEditarAcesso cEditarAcesso = new CredencialEditarAcesso();
            cEditarAcesso.acessos = credenciais.acessos;
            cEditarAcesso.cpf = cpf;
            return Response.status(200)
                .entity(cEditarAcesso)
                .build();
        }
        return Response.status(404)
            .entity(Mensagens.CPF_NAO_ENCONTRADO.getMensagem())
            .build();

    }

    @PUT
    @Path("/conceder-acessos")
    @RolesAllowed("admin")
    @Timed(value = "credencial", extraTags = {"assunto", "negocio", "categoria", "seguranca"}, percentiles = {0.95, 0.99})
    public Response atualizar(CredencialEditarAcesso credencialCamposEditaveis) {

        Credenciais credenciais = Credenciais.findByCpf(credencialCamposEditaveis.cpf);
        
        if (credenciais != null) {

            for (String acesso : credencialCamposEditaveis.acessos) {
                if (!credenciais.acessos.contains(acesso)) {
                    credenciais.acessos.add(acesso);
                }
            }

            credenciais.update();
    
            return Response.status(200)
                .entity(Mensagens.ATUALIZADO.getMensagem())
                .build();
        }

        return Response.status(404)
            .entity(Mensagens.CPF_NAO_ENCONTRADO.getMensagem())
            .build();
    }

    @PUT
    @Path("/revogar-acessos")
    @RolesAllowed("admin")
    @Timed(value = "credencial", extraTags = {"assunto", "negocio", "categoria", "seguranca"}, percentiles = {0.95, 0.99})
    public Response revogar(CredencialEditarAcesso credencialCamposEditaveis) {

        Credenciais credenciais = Credenciais.findByCpf(credencialCamposEditaveis.cpf);
        
        if (credenciais != null) {

            for (String acesso : credencialCamposEditaveis.acessos) {
                if (credenciais.acessos.contains(acesso)) {
                    credenciais.acessos.remove(acesso);
                }
            }

            credenciais.update();
    
            return Response.status(200)
                .entity(Mensagens.ATUALIZADO.getMensagem())
                .build();
        }

        return Response.status(404)
            .entity(Mensagens.CPF_NAO_ENCONTRADO.getMensagem())
            .build();
    }

    @DELETE
    @Path("/remover/{cpf}")
    @RolesAllowed("admin")
    @Timed(value = "credencial", extraTags = {"assunto", "negocio", "categoria", "seguranca"}, percentiles = {0.95, 0.99})
    public Response remover(String cpf) {
        Credenciais credenciais = Credenciais.findByCpf(cpf);

        if (credenciais != null) {
            credenciais.delete();
            Carteira carteira = Carteira.findByCpf(cpf);
            carteira.delete();

            DadosUsuario perfil = DadosUsuario.findByCpf(cpf);
            if (perfil != null) {
                perfil.delete();
                return Response.status(200)
                    .entity(Mensagens.DELETADO.getMensagem())
                    .build();
            }

            return Response.status(200)
                .entity(Mensagens.DELETADO.getMensagem())
                .build();

        }

        return Response.status(404)
            .entity(Mensagens.CPF_NAO_ENCONTRADO.getMensagem())
            .build();
    }

}
