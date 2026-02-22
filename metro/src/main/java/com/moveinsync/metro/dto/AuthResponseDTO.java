package com.moveinsync.metro.dto;

public class AuthResponseDTO {

    private String token;
    private String tokenType;
    private String role;

    public AuthResponseDTO(String token, String tokenType, String role) {
        this.token = token;
        this.tokenType = tokenType;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRole() {
        return role;
    }
}
