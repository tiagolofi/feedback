package br.gov.ma.feedback.rest;

import java.util.List;

import br.gov.ma.feedback.mensageria.Mensagens;
import br.gov.ma.feedback.mongo.Credenciais;
import br.gov.ma.feedback.mongo.Token;
import br.gov.ma.feedback.seguranca.GeradorToken;
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
    public Response geraRetornaToken(Credenciais credenciais) throws Exception { 

        if (Credenciais.verificaSenha(credenciais.cpf, credenciais.senha)) {

            credenciais.setAcessos(Credenciais.findByCpf(credenciais.cpf).acessos);

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
    public List<Token> retornaTokens(String cpf) {
        return Token.list("cpf", cpf);
    }

}
