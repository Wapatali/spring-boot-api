package io.kukua.springbootapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class TokenManager {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Long lifetime;

    public String createToken(UUID id) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + lifetime))
                .sign(Algorithm.HMAC256(secret));
    }

}
