package com.boot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.boot.dto.Mypet_UserDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Mypet_GoogleServiceImpl implements Mypet_GoogleService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${google.auth-url}")
    private String GOOGLE_AUTH_URL;
    @Value("${google.token-url}")
    private String GOOGLE_TOKEN_URL;
    @Value("${google.user-info-url}")
    private String GOOGLE_USER_INFO_URL;
    @Value("${google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    @Override
    public String getGoogleLoginURL() {
        String reqUrl = GOOGLE_AUTH_URL + "?client_id=" + GOOGLE_CLIENT_ID
                      + "&redirect_uri=" + GOOGLE_REDIRECT_URI
                      + "&response_type=code"
                      + "&scope=email%20profile%20openid";
        return reqUrl;
    }

    @Override
    public String getGoogleAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_URL, request, String.class);
            String accessToken = objectMapper.readTree(response.getBody()).path("access_token").asText();
            return accessToken;
        } catch (Exception e) {
            log.error("구글 액세스 토큰 발급 실패", e);
            return null;
        }
    }

    @Override
    public Mypet_UserDTO getGoogleUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USER_INFO_URL, HttpMethod.GET, request, String.class
            );
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            String socialId = rootNode.path("sub").asText();
            String email = rootNode.path("email").asText(null); 
            String nickname = rootNode.path("name").asText();

            Mypet_UserDTO dto = new Mypet_UserDTO();
            dto.setSocial_id(socialId);
            dto.setUser_id("google_" + socialId); 
            dto.setUser_name(nickname);
            dto.setUser_email(email); 
            dto.setSocial_type("google"); // DTO에 "google"이라고 명시
            
            return dto;
        } catch (Exception e) {
            log.error("구글 사용자 정보 조회 실패", e);
            return null;
        }
    }
}