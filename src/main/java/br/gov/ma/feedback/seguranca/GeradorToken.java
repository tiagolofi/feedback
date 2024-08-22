package br.gov.ma.feedback.seguranca;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import io.smallrye.jwt.build.Jwt;
// import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class GeradorToken {

    public String geraToken(String cpf, Set<String> acessos) {

        ZonedDateTime dataHora = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // JwtClaimsBuilder builder = Jwt.claims();
        return Jwt
            .issuer("https://feedback-issuer-404-not-found.com")
            .upn(cpf)
            .groups(acessos)
            .claim("dataHora", dataHora.format(formatter))
            .expiresIn(Duration.ofMinutes(30))
            .sign();
            
        // return builder
        //     .jwe()
        //     .header("cty", "JWT")
        //     .encrypt();

    }

}
