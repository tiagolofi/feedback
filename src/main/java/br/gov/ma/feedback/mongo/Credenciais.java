package br.gov.ma.feedback.mongo;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import static br.gov.ma.feedback.seguranca.EncriptadorSenha.encriptaSenha;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MongoEntity(collection = "usuarios")
public class Credenciais extends PanacheMongoEntity {
    
    public String cpf;
    public String senha;
    public Set<String> acessos; // admin, user, moderator, system

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

    public static boolean verificaSenha(String cpf, String senhaAberta) {
        String senhaEncriptada = encriptaSenha(senhaAberta);
        Credenciais credenciais = findByCpf(cpf);
        if (credenciais != null && credenciais.senha.equals(senhaEncriptada)){
            return true;
        }
        return false;
    }

    public static Credenciais findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }

    public boolean verificaCpfDuplicado(){
        Credenciais credenciais = findByCpf(cpf);
        if (credenciais != null && credenciais.cpf.equals(cpf)){
            return true;
        }
        return false;
    }

    public static boolean verificaSenhaLogin(String cpf, String senhaAberta) {
        String senhaEncriptada = encriptaSenha(senhaAberta);
        Credenciais credenciais = findByCpf(cpf);
        if (credenciais != null && credenciais.senha.equals(senhaEncriptada)){
            return true;
        }
        return false;
    }

}
