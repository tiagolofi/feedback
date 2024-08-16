package br.gov.ma.feedback.rest;

import java.util.List;

import br.gov.ma.feedback.mensageria.Mensagem;
import br.gov.ma.feedback.mongo.Carteira;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/carteira")
@Produces(MediaType.APPLICATION_JSON)
public class CarteiraResource {

    @GET
    @Path("/consulta/{cpf}")
    @RolesAllowed("user")
    @Timed(value = "carteira", extraTags = {"path", "/consulta/{cpf}", "assunto", "utilitario", "categoria", "developer"})
    public Carteira retornaCarteira(String cpf) {
        return Carteira.findByCpf(cpf);
    }
    
    @PUT
    @Path("/resetar")
    @RolesAllowed("system")
    @Timed(value = "carteira", extraTags = {"path", "/resetar", "assunto", "negocio", "categoria", "sistema"}, percentiles = {0.5, 0.95, 0.99})
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
