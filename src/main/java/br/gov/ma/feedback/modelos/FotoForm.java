package br.gov.ma.feedback.modelos;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import jakarta.ws.rs.core.MediaType;

public class FotoForm {
    
    @RestForm("cpf")
    @PartType(MediaType.TEXT_PLAIN)
    public String cpf;

    @RestForm("filename")
    @PartType(MediaType.TEXT_PLAIN)
    public String filename;

    @RestForm("data")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] data;

}
