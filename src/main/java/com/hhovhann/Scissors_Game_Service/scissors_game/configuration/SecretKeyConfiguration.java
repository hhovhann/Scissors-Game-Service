package com.hhovhann.Scissors_Game_Service.scissors_game.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecretKeyConfiguration {

    @Value("${app.token.secret.key}")
    private String secretKey;
    @Value("${app.token.secret.key.expiration.time}")
    private Long secretKeyExpirationTime;

    public String getSecretKey() {
        return secretKey;
    }

    public Long getSecretKeyExpirationTime() {
        return secretKeyExpirationTime;
    }
}
