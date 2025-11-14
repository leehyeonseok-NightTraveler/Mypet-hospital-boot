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

import com.boot.dao.Mypet_Kakao_DAO;
import com.boot.dto.Mypet_UserDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Mypet_KakaoServiceImpl implements Mypet_KakaoService {

    // 1. DAO 주입
    private final Mypet_Kakao_DAO kakaoDAO; 

    // 2. HTTP 통신 및 JSON 파싱을 위한 객체 (필수)
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 3. application.properties에서 설정값 주입 (필수)
    @Value("${kakao.auth-url}")
    private String KAKAO_AUTH_URL;
    
    @Value("${kakao.token-url}")
    private String KAKAO_TOKEN_URL;
    
    @Value("${kakao.user-info-url}")
    private String KAKAO_USER_INFO_URL;
    
    @Value("${kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    
    @Value("${kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    /**
     * 1. 카카오 로그인 페이지 URL 생성 (구현 완료)
     */
    @Override
    public String getKakaoLoginURL() {
        String reqUrl = KAKAO_AUTH_URL + "/oauth/authorize?client_id=" + KAKAO_CLIENT_ID
                      + "&redirect_uri=" + KAKAO_REDIRECT_URI
                      + "&response_type=code";
        
        log.info("생성된 카카오 로그인 URL: {}", reqUrl);
        return reqUrl; // 👈 null이 아님
    }

    /**
     * 2. 카카오 액세스 토큰 발급 (구현 완료)
     */
    @Override
    public String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_TOKEN_URL, request, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String accessToken = rootNode.path("access_token").asText();
            log.info("카카오 액세스 토큰 발급 성공: {}", accessToken);
            return accessToken;
        } catch (Exception e) {
            log.error("액세스 토큰 발급 실패", e);
            return null;
        }
    }

    /**
     * 3. 카카오 사용자 정보 조회 (구현 완료)
     */
    @Override
    public Mypet_UserDTO getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_USER_INFO_URL, request, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            long socialId = rootNode.path("id").asLong();
            String email = rootNode.path("kakao_account").path("email").asText(null); 
            String nickname = rootNode.path("properties").path("nickname").asText();

            Mypet_UserDTO dto = new Mypet_UserDTO();
            dto.setSocial_id(String.valueOf(socialId));
            dto.setUser_id("kakao_" + socialId); 
            dto.setUser_name(nickname);
            dto.setUser_email(email); 
            
            log.info("카카오 사용자 정보 조회 성공: {}", dto);
            return dto;

        } catch (Exception e) {
            log.error("카카오 사용자 정보 조회 실패", e);
            return null;
        }
    }

    /**
     * 4. DAO 호출 - 사용자 조회 (구현)
     */
    @Override
    public Mypet_UserDTO findUserBySocialId(String socialId) {
        log.info("DAO 호출: findUserBySocialId - {}", socialId);
        return kakaoDAO.findUserBySocialId(socialId);
    }

    /**
     * 5. DAO 호출 - 신규 회원 가입 (INSERT)
     */
    @Override
    public void socialJoin_withDetails(Mypet_UserDTO userDTO) {
        log.info("DAO 호출: socialJoin_withDetails (INSERT) - {}", userDTO.getUser_id());
        
        String fullAddress = "(" + userDTO.getUser_addr() + ") " + userDTO.getUser_addr_detail();
        userDTO.setUser_addr(fullAddress);
        userDTO.setUser_status("ACTIVE"); 
        
        kakaoDAO.socialJoin_withDetails(userDTO);
    }

    /**
     * 6. DAO 호출 - 기존 회원 정보 갱신 (UPDATE)
     */
    @Override
    public void socialUpdate_withDetails(Mypet_UserDTO userDTO) {
        log.info("DAO 호출: socialUpdate_withDetails (UPDATE) - {}", userDTO.getUser_id());
        
        String fullAddress = "(" + userDTO.getUser_addr() + ") " + userDTO.getUser_addr_detail();
        userDTO.setUser_addr(fullAddress);
        userDTO.setUser_status("ACTIVE");
        
        kakaoDAO.socialUpdate_withDetails(userDTO);
    }
}