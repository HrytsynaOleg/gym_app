package com.gym.security;

import com.gym.model.UserModel;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService {
    @Value("${token.signing.key}")
    private String signingKey;

    public String generateToken(UserModel user) {
        Map<String, Object> claims = new HashMap<>();
            claims.put("username", user.getUserName());
            claims.put("id", user.getUserId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUserName())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public String getUserName(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody()
                .get("username").toString();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
