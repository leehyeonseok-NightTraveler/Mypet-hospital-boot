package com.boot.service;

import com.boot.dto.Mypet_UserDTO;
import javax.servlet.http.HttpSession;

public interface NaverLoginService {
	// 1. 네이버 로그인 URL 생성 (세션 필요)
    String getNaverLoginURL(HttpSession session);
    
    // 2. 네이버 액세스 토큰 발급 (세션, 코드, 상태값 필요)
    String getNaverAccessToken(String code, String state, HttpSession session);
    
    // 3. 네이버 사용자 정보로 Mypet_UserDTO 생성
    Mypet_UserDTO getNaverUserInfo(String accessToken);
    
    // --- (DB 로직) ---
    
    // 4. 소셜 ID로 사용자 조회
    Mypet_UserDTO findUserBySocialId(String socialId);
    
    // 5. 신규 회원 가입
    void socialJoin_withDetails(Mypet_UserDTO userDTO); 
    
    // 6. 기존 회원 정보 갱신
    void socialUpdate_withDetails(Mypet_UserDTO userDTO);
}