package br.gov.ma.feedback.mongo;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "carteiras")
public class Carteira extends PanacheMongoEntity {
    
    public String cpf;
    public int saldoCarteira;
    public int pontosCarteira;

    public void novaCarteira(String cpf){
        this.cpf = cpf;
        this.saldoCarteira = 1000;
        persist();
    }

    public static Carteira findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }

    public void resetarCarteita(){
        this.saldoCarteira = 1000;
        update();
    }

    public void creditar(int pontuacao) {
        this.pontosCarteira += pontuacao;
        update();
    }

    public void debitar(int pontuacao){
        this.saldoCarteira -= pontuacao;
        update();
    }

}
