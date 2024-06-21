package io.kukua.springbootapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Long lifetime;

    public String createToken(String username) {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + lifetime))
                .withClaim("username", username)
                .sign(Algorithm.HMAC256(secret));
    }

}
