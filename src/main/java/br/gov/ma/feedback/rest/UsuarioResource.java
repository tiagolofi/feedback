package br.gov.ma.feedback.rest;

import java.util.List;

import br.gov.ma.feedback.modelos.Credenciais;
import br.gov.ma.feedback.modelos.Mensagens;
import br.gov.ma.feedback.modelos.Mensageria;
import br.gov.ma.feedback.mongo.Usuario;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {
    
    @POST
    @Path("/novo")
    public Response criarNovoUsuario(Usuario usuario) {
        if (!usuario.verificaUsuarioDuplicado() && usuario.validarCpf()){
            usuario.setSenhaEncriptada();
            usuario.persist();

            return Response.status(201)
                .entity(Mensagens.ADICIONADO.getMensagem())
                .build();
        }

        Mensageria mensagem = new Mensageria(String.format("Usuário já cadastrado ou CPF inválido: %s", usuario.cpf));

        return Response.status(400)
            .entity(mensagem)
            .build();
    }

    @POST
    @Path("/login")
    public Response logar(Credenciais credenciais) {
        if (Usuario.verificaUsuarioLogin(credenciais.cpf, credenciais.senha)){
            return Response.status(200)
                .entity(Mensagens.AUTORIZADO.getMensagem())
                .build();
        }
        return Response.status(401)
            .entity(Mensagens.NAO_AUTORIZADO.getMensagem())
            .build();
    }

    @PUT
    @Path("/atualizar-dados")
    public Response atualizar(Usuario usuario) {
        usuario.update();
        return Response.status(200)
            .entity(Mensagens.ATUALIZADO.getMensagem())
            .build();
    }

    @DELETE
    @Path("/apagar/{cpf}")
    public Response apagaUsuario(String cpf) {
        Usuario.apagarUsuario(cpf);
        return Response.status(200)
            .entity(Mensagens.DELETADO.getMensagem())
            .build();
    }

    @GET
    @Path("/consulta/{cpf}")
    public Usuario retornaUsuario(String cpf) {
        return Usuario.findByCpf(cpf);
    }

    @PUT
    @Path("/resetar-carteiras")
    public Response resetarCarteiras() {
        List<Usuario> usuarios = Usuario.listAll();
        for (Usuario usuario: usuarios) {
            usuario.resetarCarteita();
        }

        Mensageria mensagem = new Mensageria(String.format("%s carteiras resetadas com sucesso!", usuarios.size()));

        return Response.status(200)
            .entity(mensagem)
            .build();
    }

}
