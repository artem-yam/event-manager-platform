package dev.sorokin.eventmanager.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;

    private final Integer ttl;

    public JwtService(@Value("${jwt.key}") String jwtKey,
                      @Value("${jwt.ttl}") Integer jwtTtl) {
        this.key = Keys.hmacShaKeyFor(jwtKey.getBytes());
        this.ttl = jwtTtl;
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getAuthorities().isEmpty()
                        ? Strings.EMPTY : user.getAuthorities().iterator().next().getAuthority())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + ttl)
                )
                .signWith(key)
                .compact();
    }

    public String extractLogin(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}