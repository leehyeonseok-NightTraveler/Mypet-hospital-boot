package com.boot.service;

import com.boot.dto.NaverLoginDTO;
import com.boot.dto.NaverProfile;
import com.boot.dto.NaverUserInfoDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor // RestTemplate 빈 자동 주입
public class NaverLoginService {

    private final RestTemplate restTemplate;

    // application.properties에서 값 읽어오기
    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    @Value("${naver.authorization-uri}")
    private String authorizationUri;

    @Value("${naver.token-uri}")
    private String tokenUri;

    @Value("${naver.user-info-uri}")
    private String userInfoUri;

    /**
     * 1. 네이버 로그인 페이지 URL 생성 (컨트롤러가 호출)
     */
    public String getNaverLoginUrl(HttpSession session) {
        // CSRF 방지를 위한 state 토큰 생성
        String state = new BigInteger(130, new SecureRandom()).toString();
        // 세션에 state 저장 (콜백에서 검증하기 위함)
        session.setAttribute("naver_state", state);

        // URL 빌드
        return authorizationUri + "?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=" + state;
    }

    /**
     * 2. 코드를 이용해 Access Token 발급 (컨트롤러가 호출)
     */
    public String getAccessToken(String code, String state, HttpSession session) {
        
        // 세션에서 state 값 가져오기
        String sessionState = (String) session.getAttribute("naver_state");
        if (sessionState == null || !sessionState.equals(state)) {
            throw new RuntimeException("State 값이 일치하지 않습니다.");
        }
        
        // 요청 헤더 설정 (Content-type)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 파라미터(body) 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("state", state);

        // HttpEntity (헤더 + 파라미터) 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // API 호출 (POST)
        ResponseEntity<NaverLoginDTO> responseEntity = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                requestEntity,
                NaverLoginDTO.class // 응답을 매핑할 DTO (사용자가 만든 DTO)
        );

        // Access Token 반환
        return responseEntity.getBody().getAccessToken();
    }

    /**
     * 3. Access Token으로 사용자 정보 조회 (컨트롤러가 호출)
     */
    public NaverProfile getUserInfo(String accessToken) {
        
        // 요청 헤더 설정 (Bearer 토큰)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpEntity (헤더만) 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        
        // API 호출 (POST 또는 GET)
        ResponseEntity<NaverUserInfoDTO> responseEntity = restTemplate.exchange(
                userInfoUri,
                HttpMethod.POST, 
                requestEntity,
                NaverUserInfoDTO.class // 응답을 매핑할 래퍼 DTO
        );

        // 실제 사용자 정보(NaverProfile) 반환
        return responseEntity.getBody().getResponse();
    }
}