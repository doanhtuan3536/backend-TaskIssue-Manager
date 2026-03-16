package com.personalproject.api_gateway.controller;

import com.personalproject.api_gateway.dto.*;
import com.personalproject.api_gateway.models.OpaqueTokenResponse;
import com.personalproject.api_gateway.models.TokenInfo;
import com.personalproject.api_gateway.models.TokenPair;
import com.personalproject.api_gateway.service.OpaqueTokenExpiredException;
import com.personalproject.api_gateway.service.OpaqueTokenService;
import com.personalproject.api_gateway.serviceClient.AuthServiceClient;
import com.personalproject.api_gateway.serviceClient.JwtValidationException;
import com.personalproject.api_gateway.serviceClient.RefreshTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PostMapping("/upload/avatar")
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @CookieValue(name = "opaque_token", required = false) String opaqueToken

    ) throws IOException, OpaqueTokenExpiredException, JwtValidationException {

        System.out.println(opaqueToken);
        TokenInfo tokenInfo = tokenService.validateToken(opaqueToken);
        UploadAvatarResponse response =
                authServiceClient.uploadUserAvatar(userId, file, tokenInfo.getAccessToken());
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo( @PathVariable Long userId,
                                          @CookieValue(name = "opaque_token", required = false) String opaqueToken,
                                          HttpServletResponse httpResponse) throws OpaqueTokenExpiredException, JwtValidationException {
        if(opaqueToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        System.out.println("get user info");
        System.out.println(opaqueToken);
        TokenInfo tokenInfo = tokenService.validateToken(opaqueToken);
        User userInfo = authServiceClient.getUserInfo(userId, tokenInfo.getAccessToken());
        return ResponseEntity.ok(userInfo);


    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOpaqueToken(@CookieValue(name = "opaque_token", required = false) String opaqueToken,
                                                 HttpServletResponse httpResponse) throws OpaqueTokenExpiredException {
        System.out.println("validateOpaqueToken");

        System.out.println(opaqueToken);
        TokenInfo tokenInfo = tokenService.validateToken(opaqueToken);
        Long remainingSeconds = tokenService.getTtlSeconds(opaqueToken);
        OpaqueTokenResponse response = OpaqueTokenResponse.builder()
                .opaqueToken(opaqueToken)
                .refreshToken(tokenInfo.getRefreshToken())
                .expiresIn(remainingSeconds)
                .avatar(tokenInfo.getAvatar())
                .refreshTokenId(tokenInfo.getRefreshTokenId())
                .userId(tokenInfo.getUserId())
                .username(tokenInfo.getUsername())
                .build();
        Cookie opaqueCookie = new Cookie("opaque_token", opaqueToken);
        opaqueCookie.setHttpOnly(true);
        opaqueCookie.setSecure(true);
        opaqueCookie.setPath("/");
        opaqueCookie.setMaxAge((int)(long) remainingSeconds);
        opaqueCookie.setAttribute("SameSite", "Strict");
        httpResponse.addCookie(opaqueCookie);
        return ResponseEntity.ok(response);


    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken( @CookieValue(name = "refresh_token", required = false) String refreshToken
            ,@RequestBody RefreshTokenUsernameRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws RefreshTokenException {
//            tokenService.validateToken();
        if(refreshToken == null) {
            System.out.println("Refresh token not in cookie");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            throw new RefreshTokenException("Refresh token not in cookie");
        }
        System.out.println(refreshToken);

        AuthResponse authResponse = authServiceClient.refreshToken(new RefreshTokenRequest(request.getUsername(), refreshToken));
        System.out.println(authResponse);
        TokenPair tokenPair = tokenService.createTokens(authResponse, httpRequest);

        Cookie opaqueCookie = new Cookie("opaque_token", tokenPair.getOpaqueToken());
        opaqueCookie.setHttpOnly(true);
        opaqueCookie.setSecure(true);
        opaqueCookie.setPath("/");
        opaqueCookie.setMaxAge((int) tokenPair.getExpiresIn());
        opaqueCookie.setAttribute("SameSite", "Strict");
        httpResponse.addCookie(opaqueCookie);

        // Set refresh token in separate cookie
        Cookie refreshCookie = new Cookie("refresh_token", tokenPair.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/api/auth");
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
            opaqueCookie.setSecure(true);
            opaqueCookie.setPath("/");
            opaqueCookie.setMaxAge((int) tokenPair.getExpiresIn());
            opaqueCookie.setAttribute("SameSite", "Strict");
            httpResponse.addCookie(opaqueCookie);

            // Set refresh token in separate cookie
            Cookie refreshCookie = new Cookie("refresh_token", tokenPair.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/api/auth");
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
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {



        try {
            // Forward login request to user service
//            String url = userServiceUrl + "/api/login";
//            AuthResponse authResponse = restTemplate.postForObject(
//                    url, request, AuthResponse.class);
//            System.out.println(Arrays.toString(httpRequest.getCookies()));
            System.out.println(opaqueToken);
            System.out.println(refreshToken);

            String authResponse = authServiceClient.logout(request.getRefreshTokenId());
            tokenService.revokeToken(opaqueToken, refreshToken);

            Cookie opaqueCookie = new Cookie("opaque_token", opaqueToken);
            opaqueCookie.setHttpOnly(true);
            opaqueCookie.setSecure(true); // Set to true in production with HTTPS
            opaqueCookie.setPath("/");
            opaqueCookie.setMaxAge(0);
            opaqueCookie.setAttribute("SameSite", "Strict");
            httpResponse.addCookie(opaqueCookie);

            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/api/auth");
            refreshCookie.setMaxAge(0);
            refreshCookie.setAttribute("SameSite", "Strict");
            httpResponse.addCookie(refreshCookie);

            Cookie refreshCookie2 = new Cookie("refresh_token", refreshToken);
            refreshCookie2.setHttpOnly(true);
            refreshCookie2.setSecure(true);
            refreshCookie2.setPath("/api/auth/refresh");
            refreshCookie2.setMaxAge(0);
            refreshCookie2.setAttribute("SameSite", "Strict");
            httpResponse.addCookie(refreshCookie2);



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
