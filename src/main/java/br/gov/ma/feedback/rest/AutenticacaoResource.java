package br.gov.ma.feedback.rest;

import java.util.List;
import br.gov.ma.feedback.mensageria.Mensagens;
import br.gov.ma.feedback.modelos.CredenciaisLogin;
import br.gov.ma.feedback.mongo.Credenciais;
import br.gov.ma.feedback.mongo.Token;
import br.gov.ma.feedback.seguranca.GeradorToken;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/autenticacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AutenticacaoResource {

    @Inject
    GeradorToken geradorToken;

    @POST
    @Path("/token")
    @PermitAll
    @Timed(value = "autenticacao", extraTags = {"path", "/token", "assunto", "autenticacao", "categoria", "seguranca"}, percentiles = {0.5, 0.95, 0.99})
    public Response geraToken(CredenciaisLogin credenciaisLogin) throws Exception { 

        if (Credenciais.verificaSenha(credenciaisLogin.cpf, credenciaisLogin.senha)) {

            Credenciais credenciais = Credenciais.findByCpf(credenciaisLogin.cpf);

            if (credenciais.bloqueado) {
                return Response.status(401)
                    .entity(Mensagens.NAO_AUTORIZADO.getMensagem())
                    .build();
            }

            String tokenGerado = geradorToken.geraToken(credenciais.cpf, credenciais.acessos);

            Token token = new Token();
            token.build(credenciais.cpf, tokenGerado);
            token.persist();

            return Response.status(201)
                .entity(tokenGerado)
                .build();
        }

        return Response.status(401)
            .entity(Mensagens.NAO_AUTORIZADO.getMensagem())
            .build();

    }

    @GET
    @Path("/tokens/{cpf}")
    @RolesAllowed({"system", "admin"})
    @Timed(value = "autenticacao", extraTags = {"path", "/tokens/{cpf}", "assunto", "auditoria", "categoria", "seguranca"}, percentiles = {0.5, 0.95, 0.99})
    public List<Token> retornaTokens(String cpf) {
        return Token.list("cpf", cpf);
    }

}
