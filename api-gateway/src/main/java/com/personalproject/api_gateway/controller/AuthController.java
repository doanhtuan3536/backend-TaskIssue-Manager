package com.personalproject.api_gateway.controller;

import com.personalproject.api_gateway.dto.AuthResponse;
import com.personalproject.api_gateway.dto.LoginRequest;
import com.personalproject.api_gateway.dto.LogoutRequest;
import com.personalproject.api_gateway.models.OpaqueTokenResponse;
import com.personalproject.api_gateway.models.TokenPair;
import com.personalproject.api_gateway.service.OpaqueTokenService;
import com.personalproject.api_gateway.serviceClient.AuthServiceClient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final OpaqueTokenService tokenService;
    private final RestTemplate restTemplate;
    private final AuthServiceClient authServiceClient;

    public AuthController(OpaqueTokenService tokenService, RestTemplate restTemplate, AuthServiceClient authServiceClient) {
        this.tokenService = tokenService;
        this.restTemplate = restTemplate;
        this.authServiceClient = authServiceClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {



        try {
            // Forward login request to user service
//            String url = userServiceUrl + "/api/login";
//            AuthResponse authResponse = restTemplate.postForObject(
//                    url, request, AuthResponse.class);

            AuthResponse authResponse = authServiceClient.login(request.getUsername(), request.getPassword());
            System.out.println(authResponse);

//            if (authResponse == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }

            // Create opaque tokens
//            authResponse.setUsername(request.getUsername());
            TokenPair tokenPair = tokenService.createTokens(authResponse, httpRequest);

            // Set HTTP Only cookie for opaque token
            Cookie opaqueCookie = new Cookie("opaque_token", tokenPair.getOpaqueToken());
            opaqueCookie.setHttpOnly(true);
            opaqueCookie.setSecure(true); // Set to true in production with HTTPS
            opaqueCookie.setPath("/");
            opaqueCookie.setMaxAge((int) tokenPair.getExpiresIn());
            opaqueCookie.setAttribute("SameSite", "Strict");
            httpResponse.addCookie(opaqueCookie);

            // Set refresh token in separate cookie
            Cookie refreshCookie = new Cookie("refresh_token", tokenPair.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/api/auth/refresh");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            refreshCookie.setAttribute("SameSite", "Strict");
            httpResponse.addCookie(refreshCookie);

            OpaqueTokenResponse response = OpaqueTokenResponse.builder()
                    .opaqueToken(tokenPair.getOpaqueToken())
                    .refreshToken(tokenPair.getRefreshToken())
                    .expiresIn(tokenPair.getExpiresIn())
                    .avatar(authResponse.getAvatar())
                    .refreshTokenId(authResponse.getRefreshTokenId())
                    .userId(authResponse.getUserId())
                    .username(authResponse.getUsername())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//                    .body(ErrorResponse.builder()
//                            .status(HttpStatus.UNAUTHORIZED.value())
//                            .error("Unauthorized")
//                            .message("Invalid username or password")
//                            .build());
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @Valid @RequestBody LogoutRequest request,
            @CookieValue(name = "opaque_token", required = false) String opaqueToken,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {



        try {
            // Forward login request to user service
//            String url = userServiceUrl + "/api/login";
//            AuthResponse authResponse = restTemplate.postForObject(
//                    url, request, AuthResponse.class);
//            System.out.println(Arrays.toString(httpRequest.getCookies()));
            System.out.println(opaqueToken);

            String authResponse = authServiceClient.logout(request.getRefreshTokenId());
            tokenService.revokeToken(opaqueToken);

            Cookie opaqueCookie = new Cookie("opaque_token", opaqueToken);
            opaqueCookie.setHttpOnly(true);
            opaqueCookie.setSecure(true); // Set to true in production with HTTPS
            opaqueCookie.setPath("/");
            opaqueCookie.setMaxAge(0);
            opaqueCookie.setAttribute("SameSite", "Strict");
            httpResponse.addCookie(opaqueCookie);



            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            System.out.println("Logout failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//                    .body(ErrorResponse.builder()
//                            .status(HttpStatus.UNAUTHORIZED.value())
//                            .error("Unauthorized")
//                            .message("Invalid username or password")
//                            .build());
        }
    }
}
