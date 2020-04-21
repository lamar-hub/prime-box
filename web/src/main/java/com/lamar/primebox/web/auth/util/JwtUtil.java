package com.lamar.primebox.web.auth.util;

import com.lamar.primebox.web.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtil implements Serializable {

    private static final long serialVersionUID = -738126783528627502L;
    private static final String JWT_SECRET = "lamarlamarlamarlamarlamarlamarlamarlamarlamarlamarlamarlamar";
    private static final long JWT_TOKEN_VALIDITY = 10 * 24 * 60 * 60 * 1000;


    public String getUserIDFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getId);
    }

    public String getUsernameFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getSubject);
    }

    public String generateToken(User user) {
        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setId(user.getUserID())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET.getBytes())
                .compact();
    }

    public Boolean validateToken(String token, User user) {
        String username = this.getUsernameFromToken(token);
        Boolean expired = this.isTokenExpired(token);
        return username.equals(user.getUsername()) && !expired;
    }

    private Boolean isTokenExpired(String token) {
        Date expDate = this.getExpirationDateFromToken(token);
        return expDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(JWT_SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
