package br.gov.ma.feedback.security;

import java.io.IOException;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FiltroAutorizacao implements ContainerRequestFilter {

    @ConfigProperty(name = "TOKEN_AUTORIZACAO")
    private String tokenAutorizacao;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.equals("Bearer " + tokenAutorizacao)) {
            requestContext.abortWith(Response.status(401).build());
        }
    }

}
