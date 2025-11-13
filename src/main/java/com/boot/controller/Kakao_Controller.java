package com.boot.controller; // 👈 본인의 controller 패키지 경로

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boot.dto.Mypet_UserDTO;
import com.boot.service.Mypet_KakaoService; // 👈 인터페이스 주입

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class Kakao_Controller {

    private final Mypet_KakaoService kakaoService; // 👈 인터페이스 주입

    // 1. "카카오 로그인" 버튼을 눌렀을 때 호출될 주소
    // (이 주소로 요청하면 서비스가 만든 카카오 인증 URL로 리다이렉트됨)
    @GetMapping("/auth/kakao/login")
    public String kakaoLogin() {
        String kakaoAuthUrl = kakaoService.getKakaoLoginURL();
        log.info("카카오 인증 페이지로 리다이렉트: {}", kakaoAuthUrl);
        return "redirect:" + kakaoAuthUrl;
    }

    // 2. 카카오 서버가 인증 코드를 보내줄 Redirect URI
    // (application.properties, 카카오 개발자 설정, 이 컨트롤러 3곳의 주소가 모두 일치해야 함)
    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam String code, HttpSession session) {
        
        log.info("카카오 콜백 수신, 인증 코드: {}", code);
        
        // 1. 인증 코드로 액세스 토큰 받기
        String accessToken = kakaoService.getKakaoAccessToken(code);
        
        // 2. 액세스 토큰으로 사용자 정보 받기
        Mypet_UserDTO userInfo = kakaoService.getKakaoUserInfo(accessToken);
        
        // 3. DB에서 사용자 정보 확인
        Mypet_UserDTO loginUser = kakaoService.findUserBySocialId(userInfo.getSocial_id());

        if (loginUser == null) {
            // 4. 신규 사용자인 경우, 자동 회원가입
            // (이메일이 null일 수 있으니 DTO의 user_email 필드가 null을 허용해야 함)
            kakaoService.socialJoin(userInfo);
            loginUser = userInfo; // 새로 가입한 정보를 로그인 정보로 사용
            log.info("카카오 신규 회원 자동 가입 완료: {}", loginUser.getUser_id());
        }

        // 5. 로그인 성공 (세션 생성)
        session.setAttribute("loginUser", loginUser);
        log.info("카카오 로그인 성공. 세션 생성 완료: {}", loginUser.getUser_id());

        return "redirect:/login_ok_mainpage"; // 로그인 후 이동할 메인 페이지
    }
}