package br.gov.ma.feedback.mongo;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import static br.gov.ma.feedback.security.EncriptadorSenha.encriptaSenha;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MongoEntity(collection = "usuarios")
public class Usuario extends PanacheMongoEntity {
    
    public String nome;
    public String cpf;
    public String senha;
    public int carteira;
    public int bonificacao;

    public void setSenhaEncriptada() {
        this.senha = encriptaSenha(senha);
    }

    public boolean validarCpf() {
        Pattern pattern = Pattern.compile("[\\d{11}]+");
        Matcher matcher = pattern.matcher(cpf);
        if (matcher.matches()){
            return true;
        }
        return false;
    }

    public void resetarCarteita(){
        this.carteira = 1000;
        update();
    }

    public void creditar(int pontuacao) {
        this.bonificacao += pontuacao;
    }

    public void debitar(int pontuacao){
        this.carteira -= pontuacao;
    }

    public static Usuario findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }

    public boolean verificaUsuarioDuplicado(){
        Usuario usuarioCpf = findByCpf(cpf);
        if (usuarioCpf != null && usuarioCpf.cpf.equals(cpf)){
            return true;
        }
        return false;
    }

    public static boolean verificaUsuarioLogin(String cpf, String senhaAberta) {
        String senhaEncriptada = encriptaSenha(senhaAberta);
        Usuario usuarioCpf = findByCpf(cpf);
        if (usuarioCpf != null && usuarioCpf.senha.equals(senhaEncriptada)){
            return true;
        }
        return false;
    }

    public static void apagarUsuario(String cpf) {
        delete("cpf", cpf);
    }

}
