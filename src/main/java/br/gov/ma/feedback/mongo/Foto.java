package br.gov.ma.feedback.mongo;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "fotos")
public class Foto extends PanacheMongoEntity {
    
    public String cpf;
    public String filename;
    public byte[] data;

    public static Foto findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }

}
