package com.microservice.educationPortal.authmanager.payload;

/**
 * This class models the response that is given for returning a JWT token
 * to user by the HTTP header:
 * Authorization: Bearer <JWT>
 */
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";


    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
