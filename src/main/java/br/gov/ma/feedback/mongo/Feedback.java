package br.gov.ma.feedback.mongo;

import java.util.List;

import org.bson.types.ObjectId;

import br.gov.ma.feedback.modelos.Comentario;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "feedbacks")
public class Feedback extends PanacheMongoEntity {
    
    public String cpfDestino;
    public String cpfRemetente;
    public Comentario comentario;
    public int pontuacao;

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
