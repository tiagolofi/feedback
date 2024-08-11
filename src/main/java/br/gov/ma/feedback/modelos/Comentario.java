package br.gov.ma.feedback.modelos;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Comentario {
    
    public String comentario;
    public String dataHora;

    public void setDataHora() {
        ZonedDateTime dataHora = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dataHora = dataHora.format(formatter);
    }

}
