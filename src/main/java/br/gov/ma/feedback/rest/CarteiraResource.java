package br.gov.ma.feedback.rest;

import java.util.List;

import br.gov.ma.feedback.mensageria.Mensagem;
import br.gov.ma.feedback.mongo.Carteira;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/carteira")
@Produces(MediaType.APPLICATION_JSON)
public class CarteiraResource {
    
    @PUT
    @Path("/resetar")
    @RolesAllowed("system")
    public Response resetarCarteiras() {
        List<Carteira> carteiras = Carteira.listAll();
        for (Carteira carteira: carteiras) {
            carteira.resetarCarteita();
        }

        Mensagem mensagem = new Mensagem(String.format("%s carteiras resetadas com sucesso!", carteiras.size()));

        return Response.status(200)
            .entity(mensagem)
            .build();
    }

}
