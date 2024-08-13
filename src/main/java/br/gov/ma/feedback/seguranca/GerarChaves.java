package br.gov.ma.feedback.seguranca;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class GerarChaves {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // Gerar o par de chaves
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // Salvar a chave privada em um arquivo
        try (FileOutputStream fos = new FileOutputStream("privateKey.pem")) {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            fos.write(Base64.getEncoder().encode(spec.getEncoded()));
        }

        // Salvar a chave pública em um arquivo
        try (FileOutputStream fos = new FileOutputStream("publicKey.pem")) {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
            fos.write(Base64.getEncoder().encode(spec.getEncoded()));
        }
    }
}