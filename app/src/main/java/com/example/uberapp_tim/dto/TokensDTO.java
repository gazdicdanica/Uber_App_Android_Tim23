package com.example.uberapp_tim.dto;

public class TokensDTO {
    private String accessToken;
    private Long expiresIn;

    public TokensDTO(String token, Long expiresIn){
        this.accessToken = token;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
