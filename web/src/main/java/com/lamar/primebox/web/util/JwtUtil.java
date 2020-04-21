package com.lamar.primebox.web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    private final JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String extractUserIDFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getId);
    }

    public String extractUsernameFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getSubject);
    }

    public String generateToken(String userId, String username) {
        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setId(userId)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getValidInMillis()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes())
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        String usernameFromToken = this.extractUsernameFromToken(token);
        Boolean expired = this.isTokenExpired(usernameFromToken);
        return usernameFromToken.equals(username) && !expired;
    }

    private Boolean isTokenExpired(String token) {
        Date expDate = this.extractExpirationDateFromToken(token);
        return expDate.before(new Date());
    }

    private Date extractExpirationDateFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(jwtProperties.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
