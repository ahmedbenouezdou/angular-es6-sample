package com.example.securitydemo.auth;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.OAuth2TokenValidator;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@Component
public class GoogleTokenVerifier implements JwtDecoder {

    private final NimbusJwtDecoder jwtDecoder;

    public GoogleTokenVerifier(@Value("${security.google.issuer:https://accounts.google.com}") String issuer,
                               @Value("${security.google.jwks:https://www.googleapis.com/oauth2/v3/certs}") String jwksUri)
            throws MalformedURLException {
        JWKSource<SecurityContext> jwkSource = new RemoteJWKSet<>(new URL(jwksUri));
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSource(jwkSource).build();
        OAuth2TokenValidator<Jwt> validator = JwtValidators.createDefaultWithIssuer(issuer);
        decoder.setJwtValidator(validator);
        decoder.setClaimSetConverter(new GoogleJwtClaimConverter());
        decoder.setClockSkew(Duration.ofMinutes(2));
        this.jwtDecoder = decoder;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        return jwtDecoder.decode(token);
    }
}
