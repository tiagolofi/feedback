package br.gov.ma.feedback.seguranca;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GeradorToken {

    @ConfigProperty(name = "PRIVATE_KEY")
    String privateEnvKey;

    public String geraToken(String cpf, Set<String> acessos) throws Exception {

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateEnvKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(spec);

        ZonedDateTime dataHora = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return Jwt.issuer("https://feedback-issuer-404-not-found.com")
            .upn(cpf)
            .groups(acessos)
            .claim("dataHora", dataHora.format(formatter))
            .expiresIn(Duration.ofMinutes(30))
            .sign(privateKey);

    }


}
