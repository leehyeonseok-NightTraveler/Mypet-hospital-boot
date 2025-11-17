package com.boot.controller;

import com.boot.dto.NaverProfile;
import com.boot.service.NaverLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor // final 필드 생성자 자동 주입
@RequestMapping("/auth") // URL을 /auth 로 묶습니다.
public class NaverLoginController {

    private final NaverLoginService naverLoginService;
    private final HttpSession httpSession; // 세션 주입

    /**
     * 1. 네이버 로그인 페이지로 리다이렉트
     * JSP에서 <a href="/auth/naver">네이버로 로그인</a> 링크를 클릭하면 이 메소드가 호출됩니다.
     */
    @GetMapping("/naver")
    public String redirectToNaverLogin() {
        // Service에서 네이버 로그인 URL을 받아옵니다. (state 값은 서비스 내부에서 세션에 저장)
        String naverLoginUrl = naverLoginService.getNaverLoginUrl(httpSession);
        
        // 네이버 로그인 페이지로 리다이렉트
        return "redirect:" + naverLoginUrl; 
    }

    /**
     * 2. 네이버 로그인 성공 후 콜백 처리
     * (네이버 개발자 센터에 등록한 "http://localhost:8686/auth/naver/callback" 주소)
     */
    @GetMapping("/naver/callback")
    public String naverCallback(@RequestParam String code, @RequestParam String state) {
        
        try {
            // 1. 코드를 이용해 Access Token 받기 (state 검증 포함)
            String accessToken = naverLoginService.getAccessToken(code, state, httpSession);

            // 2. Access Token을 이용해 사용자 정보 받기
            NaverProfile userInfo = naverLoginService.getUserInfo(accessToken);

            // 3. 세션에 사용자 정보 저장 (로그인 처리)
            // (실제로는 DB에서 회원인지 확인 후, 회원가입 또는 로그인 처리를 합니다)
            
            // "loginUser"라는 이름으로 사용자 프로필(NaverProfile)을 세션에 저장
            httpSession.setAttribute("loginUser", userInfo); 
            // 로그인 타입을 "naver"로 저장 (선택 사항)
            httpSession.setAttribute("loginType", "naver");
            
            httpSession.setAttribute("role", "USER");

            System.out.println("네이버 로그인 성공: " + userInfo.getName());

        } catch (Exception e) {
            System.err.println("네이버 로그인 실패: " + e.getMessage());
            e.printStackTrace();
            
            // 에러 발생 시 로그인 페이지로 리다이렉트 (경로는 실제 로그인 폼 URL로 수정하세요)
            return "redirect:/loginForm"; 
        }

        // 로그인 성공 시 메인 페이지("/")로 리다이렉트
        return "redirect:/";
    }
}