package br.gov.ma.feedback.mongo;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "fotos")
public class Foto extends PanacheMongoEntity {
    
    public String cpf;
    public String filename;
    public String contentType;
    public byte[] data;

    public void setFilename() {
        this.filename = this.cpf + "." + this.contentType;
    }

    public static Foto findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }

}
