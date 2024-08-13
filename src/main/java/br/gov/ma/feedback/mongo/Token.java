package br.gov.ma.feedback.mongo;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "tokens")
public class Token extends PanacheMongoEntity {
    
    public String cpf;
    public String token;

    public void build(String cpf, String token) {
        this.cpf = cpf;
        this.token = token;
    }

}
