package br.gov.ma.feedback.rest;

import br.gov.ma.feedback.mensageria.Mensagem;
import br.gov.ma.feedback.mensageria.Mensagens;
import br.gov.ma.feedback.modelos.FotoForm;
import br.gov.ma.feedback.mongo.Foto;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/foto")
@RequestScoped
public class FotosResource {

    @POST
    @Path("/nova")
    @RolesAllowed("user")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(value = "foto", extraTags = {"path", "/foto/nova", "assunto", "negocio", "categoria", "cliente"}, percentiles = {0.5, 0.95, 0.99})
    public Response uploadFoto(FotoForm form){

        try {
            Foto foto = new Foto();

            foto.cpf = form.cpf;
            foto.contentType = form.contentType;
            foto.data = form.data;
            foto.setFilename();
            foto.persist();
    
            return Response.status(201)
                .entity(Mensagens.ADICIONADO.getMensagem())
                .build();

        } catch (Exception e) {

            Mensagem mensagem = new Mensagem(String.format("Ocorreu um erro: %s", e.getMessage()));

            return Response.status(500)
                .entity(mensagem)
                .build();
        }
    }

    @GET
    @Path("/consulta/{cpf}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Timed(value = "foto", extraTags = {"path", "/consulta/{cpf}", "assunto", "negocio", "categoria", "cliente"}, percentiles = {0.5, 0.95, 0.99})
    public Response retornaFoto(String cpf) {
        Foto foto = Foto.findByCpf(cpf);
        if (foto != null) {
            return Response.status(200)
                .header("Content-Disposition", "attachment; filename=\"" + foto.filename + "\"")
                .entity(foto.data)
                .build();
        }
        return Response.status(404)
            .entity(new Mensagem("Foto não encontrada"))
            .build();
    }

    @DELETE
    @Path("/remover/{cpf}")
    @RolesAllowed("user")
    @Timed(value = "foto", extraTags = {"path", "/remover/{cpf}", "assunto", "negocio", "categoria", "cliente"}, percentiles = {0.5, 0.95, 0.99})
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFoto(String cpf) {
        Foto foto = Foto.findByCpf(cpf);
        foto.delete();
        return Response.status(200)
            .entity(Mensagens.DELETADO.getMensagem())
            .build(); 
    }
}
