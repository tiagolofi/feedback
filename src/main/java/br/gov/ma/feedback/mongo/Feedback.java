package br.gov.ma.feedback.mongo;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "feedbacks")
public class Feedback extends PanacheMongoEntity {
    
    public String cpfDestino;
    public String cpfRemetente;
    public String comentario;
    public String tipoReconhecimento;
    public String dataHora;
    public int pontuacao;

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setTipoReconhecimento(String tipoReconhecimento) {
        this.tipoReconhecimento = tipoReconhecimento;
    }

    public void setDataHora() {
        ZonedDateTime dataHora = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dataHora = dataHora.format(formatter);
    }

    public static List<Feedback> listarComentariosByCpfDestino(String cpf) {
        return Feedback.list("cpfDestino", cpf);
    }

    public static List<Feedback> listarComentariosByCpfRemetente(String cpf) {
        return Feedback.list("cpfRemetente", cpf);
    }

    public static Feedback listarComentarioById(String id){
        return findById(new ObjectId(id));
    }

}
