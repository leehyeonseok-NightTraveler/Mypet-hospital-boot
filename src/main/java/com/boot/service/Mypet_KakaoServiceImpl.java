package com.boot.service;

import java.util.Map;

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

import com.boot.dao.Mypet_Kakao_DAO; // 👈 실제 DAO 클래스
import com.boot.dto.Mypet_UserDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Mypet_KakaoServiceImpl implements Mypet_KakaoService {

    // 1. DAO 주입 (MyBatis 사용)
    private final Mypet_Kakao_DAO kakaoDAO; 

    // 2. HTTP 통신을 위한 RestTemplate 주입
    private final RestTemplate restTemplate = new RestTemplate(); // 간단하게 여기서 생성
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱용
    
    // 3. application.properties에서 설정값 주입받기
    @Value("${kakao.auth-url}")
    private String KAKAO_AUTH_URL;
    
    @Value("${kakao.token-url}")
    private String KAKAO_TOKEN_URL;
    
    @Value("${kakao.user-info-url}")
    private String KAKAO_USER_INFO_URL;
    
    @Value("${kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    
    @Value("${kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI; // 👈 이 값이 핵심!

    /**
     * 1. 카카오 로그인 페이지 URL 생성
     */
    @Override
    public String getKakaoLoginURL() {
        // application.properties에 등록된 KAKAO_REDIRECT_URI 값을 사용!
        String reqUrl = KAKAO_AUTH_URL + "/oauth/authorize?client_id=" + KAKAO_CLIENT_ID
                      + "&redirect_uri=" + KAKAO_REDIRECT_URI
                      + "&response_type=code";
        
        log.info("생성된 카카오 로그인 URL: {}", reqUrl); // 👈 이 로그 확인
        return reqUrl;
    }

    /**
     * 2. 카카오 액세스 토큰 발급
     */
    @Override
    public String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KAKAO_REDIRECT_URI); // 👈 여기도 동일한 값 사용
        params.add("code", code);
        // Client Secret은 필수가 아닐 수 있음 (설정에 따라)

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
     * 3. 카카오 사용자 정보 조회
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
            String email = rootNode.path("kakao_account").path("email").asText(null); // 이메일은 선택 동의 항목
            String nickname = rootNode.path("properties").path("nickname").asText();

            Mypet_UserDTO dto = new Mypet_UserDTO();
            dto.setSocial_id(String.valueOf(socialId)); // socialId는 문자열(string)로
            dto.setUser_id("kakao_" + socialId); // 임시 ID 생성
            dto.setUser_name(nickname);
            dto.setUser_email(email); // 이메일이 null일 수 있음
            
            log.info("카카오 사용자 정보 조회 성공: {}", dto);
            return dto;

        } catch (Exception e) {
            log.error("카카오 사용자 정보 조회 실패", e);
            return null;
        }
    }

    /**
     * 4. DAO 호출 - 사용자 조회
     */
    @Override
    public Mypet_UserDTO findUserBySocialId(String socialId) {
        log.info("DAO 호출: findUserBySocialId - {}", socialId);
        return kakaoDAO.findUserBySocialId(socialId);
    }

    /**
     * 5. DAO 호출 - 회원가입
     */
    @Override
    public void socialJoin(Mypet_UserDTO userDTO) {
        log.info("DAO 호출: socialJoin - {}", userDTO.getUser_id());
        kakaoDAO.socialJoin(userDTO);
    }
}