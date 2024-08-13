package br.gov.ma.feedback.mongo;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "dadosUsuario")
public class DadosUsuario extends PanacheMongoEntity {
    
    public String cpf;
    public String matricula;
    public String nome;
    public String apelido;
    public String adjunta;
    public String cargo;
    public String setor;

    public static DadosUsuario findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }

}
