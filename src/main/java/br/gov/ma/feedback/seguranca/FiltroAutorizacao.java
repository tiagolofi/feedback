package br.gov.ma.feedback.seguranca;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import br.gov.ma.feedback.mensageria.Mensagens;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class FiltroAutorizacao implements ContainerRequestFilter {

    @ConfigProperty(name = "TOKEN_AUTORIZACAO") // liberação de consumo por outra aplicação
    private String tokenAutorizacao;

    @Inject
    JsonWebToken jwt;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String tokenAutorizacaoHeader = requestContext.getHeaderString("Token-Autorizacao");

        if (tokenAutorizacaoHeader == null || !tokenAutorizacaoHeader.equals(tokenAutorizacao)) {
            requestContext.abortWith(Response.status(401)
                .entity(Mensagens.NAO_AUTORIZADO_HEADER.getMensagem())
                .build());
        }

        List<String> paths = Arrays.asList("/usuario/novo", "/autenticacao/token", "/usuario/solicita-troca-senha/", "/troca-senha");
        String path = requestContext.getUriInfo().getPath();

        if (paths.stream().noneMatch(path::contains)) {

            long expiresIn = jwt.getExpirationTime();
            long now = Instant.now().getEpochSecond();
            
            if (expiresIn < now) {
                requestContext.abortWith(Response.status(401)
                    .entity(Mensagens.TOKEN_EXPIRADO.getMensagem())
                    .build());
            }

        }
    }
}
