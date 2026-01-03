package com.lucasgiavaroti.usuario.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtUtil {

    // Chave secreta usada para assinar e verificar tokens JWT
    private static final String SECRET = "9bcd498cf3c664639d61d91ad13c37471faf9829128168a8ad219f2edcfe23a60541e2202e420837ecb22c322caf4bb61845a0ac52a32e74b7c2022164a9f46243f36492ca503f524db2e91046afddb662094886d3a9bb2ffc9a8dc3f7419130671eefd27ab15630a7fec376fc19c5776624643771236b38e7a395d14d74db8c0634558d1347c6d3e71bfcd23448f21a423945a1061cc8434b78ba548838597f9cb0fae790947be7dd3fca11e32ba5e5720e5ed8fc2c13ad7f395500b3191dbf43f7f9578ab7106bbfc6407a7f05dbec5760da189603397f6cb87e94801721845b702654acb119a1833287d5b6701d62be5f4007f5affe2078167dd216904923";

    private SecretKey getSecretKey() {
       byte[] keyBytes = Base64.getDecoder().decode(SECRET);
       return Keys.hmacShaKeyFor(keyBytes);
    }

    // Gera um token JWT com o nome de usuário e validade de 1 hora
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username) // Define o email de usuário como o assunto do token
                .issuedAt(new Date()) // Define a data e hora de emissão do token
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Define a data e hora de expiração (1 hora a partir da emissão)
                .signWith(getSecretKey()) // Utiliza a secret key para assinar o token
                .compact(); // Constrói o token JWT
    }

    // Extrai as claims do token JWT (informações adicionais do token)
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey()) // Define a chave secreta para validar a assinatura do token
                .build()
                .parseSignedClaims(token) // Analisa o token JWT e obtém as claims
                .getPayload(); // Retorna o corpo das claims
    }

    // Extrai o email de usuário do token JWT
    public String extractEmail(String token) {
        // Obtém o assunto (nome de usuário) das claims do token
        return extractClaims(token).getSubject();
    }

    // Verifica se o token JWT está expirado
    public boolean isTokenExpired(String token) {
        // Compara a data de expiração do token com a data atual
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Valida o token JWT verificando o nome de usuário e se o token não está expirado
    public boolean validateToken(String token, String username) {
        // Extrai o nome de usuário do token
        final String extractedUsername = extractEmail(token);
        // Verifica se o nome de usuário do token corresponde ao fornecido e se o token não está expirado
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
