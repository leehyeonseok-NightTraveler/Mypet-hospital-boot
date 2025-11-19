package com.boot.service;

import com.boot.dao.NaverLoginDAO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.dto.NaverLoginDTO;
import com.boot.dto.NaverProfile;
import com.boot.dto.NaverUserInfoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@RequiredArgsConstructor // 1. final DAO 주입
@Slf4j
public class NaverLoginServiceImpl implements NaverLoginService {// 1. DAO 주입
    private final NaverLoginDAO naverLoginDAO;

    // 2. HTTP 통신 (카카오 예제와 동일하게 field로 선언)
    private final RestTemplate restTemplate = new RestTemplate();
    
    // 3. application.properties에서 설정값 주입
    @Value("${naver.client-id}")
    private String NAVER_CLIENT_ID;
    
    @Value("${naver.client-secret}")
    private String NAVER_CLIENT_SECRET;
    
    @Value("${naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;
    
    @Value("${naver.authorization-uri}")
    private String NAVER_AUTH_URL;
    
    @Value("${naver.token-uri}")
    private String NAVER_TOKEN_URL;
    
    @Value("${naver.user-info-uri}")
    private String NAVER_USER_INFO_URL;


    /**
     * 1. 네이버 로그인 페이지 URL 생성
     */
    @Override
    public String getNaverLoginURL(HttpSession session) {
        String state = new BigInteger(130, new SecureRandom()).toString();
        session.setAttribute("naver_state", state);
        
        String reqUrl = NAVER_AUTH_URL + "?response_type=code"
                + "&client_id=" + NAVER_CLIENT_ID
                + "&redirect_uri=" + NAVER_REDIRECT_URI
                + "&state=" + state;
        
        log.info("생성된 네이버 로그인 URL: {}", reqUrl);
        return reqUrl;
    }

    /**
     * 2. 네이버 액세스 토큰 발급
     */
    @Override
    public String getNaverAccessToken(String code, String state, HttpSession session) {
        String sessionState = (String) session.getAttribute("naver_state");
        if (sessionState == null || !sessionState.equals(state)) {
            log.error("네이버 state 값이 일치하지 않습니다.");
            throw new RuntimeException("State 값이 일치하지 않습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("redirect_uri", NAVER_REDIRECT_URI);
        params.add("code", code);
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<NaverLoginDTO> response = restTemplate.postForEntity(NAVER_TOKEN_URL, request, NaverLoginDTO.class);
            String accessToken = response.getBody().getAccessToken();
            log.info("네이버 액세스 토큰 발급 성공: {}", accessToken);
            return accessToken;
        } catch (Exception e) {
            log.error("네이버 액세스 토큰 발급 실패", e);
            return null;
        }
    }

    /**
     * 3. 네이버 사용자 정보 조회 (Mypet_UserDTO로 변환)
     */
    @Override
    public Mypet_UserDTO getNaverUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<NaverUserInfoDTO> response = restTemplate.postForEntity(NAVER_USER_INFO_URL, request, NaverUserInfoDTO.class);
            
            // 실제 프로필 정보
            NaverProfile naverProfile = response.getBody().getResponse(); 
            
            // (카카오 패턴처럼) Mypet_UserDTO로 변환
            Mypet_UserDTO dto = new Mypet_UserDTO();
            dto.setSocial_id(naverProfile.getId());
            dto.setUser_id("naver_" + naverProfile.getId()); // 고유 ID 조합
            dto.setUser_name(naverProfile.getName());
            dto.setUser_email(naverProfile.getEmail());
            dto.setUser_img(naverProfile.getProfile_image()); // 프로필 이미지
            
            log.info("네이버 사용자 정보 조회 성공: {}", dto);
            return dto;

        } catch (Exception e) {
            log.error("네이버 사용자 정보 조회 실패", e);
            return null;
        }
    }

    /**
     * 4. DAO 호출 - 사용자 조회
     */
    @Override
    public Mypet_UserDTO findUserBySocialId(String socialId) {
        log.info("DAO 호출: findUserBySocialId - {}", socialId);
        return naverLoginDAO.findUserBySocialId(socialId);
    }

    /**
     * 5. DAO 호출 - 신규 회원 가입 (INSERT)
     */
    @Override
    public void socialJoin_withDetails(Mypet_UserDTO userDTO) {
        log.info("DAO 호출: socialJoin_withDetails (INSERT) - {}", userDTO.getUser_id());
        
        // (카카오 로직과 동일하게 주소 처리, 상태 설정)
        String fullAddress = "(" + userDTO.getUser_addr() + ") " + userDTO.getUser_addr_detail();
        userDTO.setUser_addr(fullAddress);
        userDTO.setUser_status("ACTIVE");
        userDTO.setSocial_type("naver"); // ⭐️ 소셜 타입 설정
        
        naverLoginDAO.socialJoin_withDetails(userDTO);
    }

    /**
     * 6. DAO 호출 - 기존 회원 정보 갱신 (UPDATE)
     */
    @Override
    public void socialUpdate_withDetails(Mypet_UserDTO userDTO) {
        log.info("DAO 호출: socialUpdate_withDetails (UPDATE) - {}", userDTO.getUser_id());
        
        // (카카오 로직과 동일하게 주소 처리, 상태 설정)
        String fullAddress = "(" + userDTO.getUser_addr() + ") " + userDTO.getUser_addr_detail();
        userDTO.setUser_addr(fullAddress);
        userDTO.setUser_status("ACTIVE");
        userDTO.setSocial_type("naver"); // ⭐️ 소셜 타입 설정
        
        naverLoginDAO.socialUpdate_withDetails(userDTO);
    }
}