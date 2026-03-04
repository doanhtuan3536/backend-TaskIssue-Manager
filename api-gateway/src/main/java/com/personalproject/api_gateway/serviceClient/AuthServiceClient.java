package com.personalproject.api_gateway.serviceClient;

import com.personalproject.api_gateway.dto.AuthResponse;
import com.personalproject.api_gateway.dto.LoginRequest;
import com.personalproject.api_gateway.dto.LogoutRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthServiceClient {
    private String serviceURL = "http://user-service/api/v1/users";
    private final String serviceLoginUrl = serviceURL + "/login";
    private final String serviceLogoutUrl = serviceURL + "/logout";
    @Autowired
    private RestTemplate restTemplate;

    public AuthResponse login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginRequest form = new LoginRequest();
        form.setUsername(username);
        form.setPassword(password);

        HttpEntity<LoginRequest> request = new HttpEntity<>(form, headers);
//        System.out.println(request);
        ResponseEntity<AuthResponse> response = null;

        try{
            response= restTemplate.exchange(
                    serviceLoginUrl, HttpMethod.POST, request, AuthResponse.class
            );
            System.out.println(response.getBody());
            return response.getBody();
        }
        catch(HttpClientErrorException e){
            throw new RuntimeException("Authentication failed");
        }
    }
    public String logout(Integer refreshTokenId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LogoutRequest form = new LogoutRequest(refreshTokenId);
//        form.setRefreshToken(refreshtoken);

        HttpEntity<LogoutRequest> request = new HttpEntity<>(form, headers);
//        System.out.println(request);
        ResponseEntity<Void> response = null;

        try{
            response= restTemplate.exchange(
                    serviceLogoutUrl, HttpMethod.POST, request, Void.class
            );
            System.out.println(response);
            return "Logout successful";
        }
        catch(HttpClientErrorException e){
            throw new RuntimeException("Logout failed");
        }
    }
}
