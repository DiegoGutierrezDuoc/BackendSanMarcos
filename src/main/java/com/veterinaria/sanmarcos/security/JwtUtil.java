package com.veterinaria.sanmarcos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "VeterinariaSanMarcosClaveSecretaSuperSegura2026!";
    private static final long EXPIRATION_MS = 86400000;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(String correo, String rol) {
        return Jwts.builder()
                .subject(correo)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    public String extraerCorreo(String token) {
        return extraerClaims(token).getSubject();
    }

    public String extraerRol(String token) {
        return extraerClaims(token).get("rol", String.class);
    }

    public boolean validarToken(String token) {
        try {
            Claims claims = extraerClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}