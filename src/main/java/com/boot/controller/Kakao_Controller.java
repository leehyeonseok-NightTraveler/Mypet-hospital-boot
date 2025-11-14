package com.boot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.dto.Mypet_UserDTO;
import com.boot.service.Mypet_KakaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class Kakao_Controller {

    private final Mypet_KakaoService kakaoService;

    // 1. "카카오 로그인" 버튼 클릭 시 (수정 없음)
    @GetMapping("/auth/kakao/login")
    public String kakaoLogin() {
        String kakaoAuthUrl = kakaoService.getKakaoLoginURL();
        log.info("카카오 인증 페이지로 리다이렉트: {}", kakaoAuthUrl);
        return "redirect:" + kakaoAuthUrl;
    }

    /**
     * 2. 카카오 콜백 처리 (기존/신규 회원 분기)
     */
    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam String code, HttpSession session, RedirectAttributes rttr) {
        
        String accessToken = kakaoService.getKakaoAccessToken(code);
        Mypet_UserDTO userInfo = kakaoService.getKakaoUserInfo(accessToken);
        
        if (userInfo == null || userInfo.getSocial_id() == null) {
             rttr.addFlashAttribute("message", "카카오 로그인에 실패했습니다.");
             return "redirect:/login";
        }

        Mypet_UserDTO loginUser = kakaoService.findUserBySocialId(userInfo.getSocial_id());

        if (loginUser == null) {
            // [CASE 1: 신규 회원]
            session.setAttribute("temp_kakao_user", userInfo);
            log.info("신규 카카오 회원. 추가 정보 입력 페이지로 이동.");
            return "redirect:/register_social"; 
            
        } else {
            // [CASE 2: 기존 회원]
            // 휴대폰 번호가 없는 경우 (추가 정보 필요)
            if (loginUser.getUser_phone() == null || loginUser.getUser_phone().isEmpty()) {
                session.setAttribute("temp_kakao_user", loginUser);
                log.info("기존 회원(휴대폰 정보 없음). 추가 정보 입력 페이지로 이동.");
                return "redirect:/register_social";
                
            } else {
                // [CASE 2-2: 완전한 기존 회원]
                session.setAttribute("loginUser", loginUser);

                // 🔻🔻🔻 [수정됨] 헤더가 인식할 수 있도록 "USER"로 저장 🔻🔻🔻
                session.setAttribute("role", "USER"); 
                
                log.info("기존 카카오 회원 로그인 성공. 세션 생성 완료: {}", loginUser.getUser_id());
                return "redirect:/mainpage";
            }
        }
    }

    /**
     * 3. 추가 정보 입력 폼 페이지 (GET) (수정 없음)
     */
    @GetMapping("/register_social")
    public String showSocialRegisterForm(HttpSession session, Model model, RedirectAttributes rttr) {
        
        Mypet_UserDTO tempUser = (Mypet_UserDTO) session.getAttribute("temp_kakao_user");
        
        if (tempUser == null) {
            rttr.addFlashAttribute("message", "로그인 세션이 만료되었습니다.");
            return "redirect:/login";
        }
        
        model.addAttribute("userDTO", tempUser);
        return "register_social";
    }

    /**
     * 4. 추가 정보 폼 제출 처리 (POST)
     */
    @PostMapping("/register_social_process")
    public String processSocialRegister(@ModelAttribute Mypet_UserDTO formData, HttpSession session, RedirectAttributes rttr) {
        
        Mypet_UserDTO tempUser = (Mypet_UserDTO) session.getAttribute("temp_kakao_user");
        
        if (tempUser == null) {
            rttr.addFlashAttribute("message", "로그인 세션이 만료되었습니다.");
            return "redirect:/login";
        }
        
        // 폼데이터(formData)를 세션정보(tempUser)에 덮어쓰기
        tempUser.setUser_phone(formData.getUser_phone());
        tempUser.setUser_gender(formData.getUser_gender());
        tempUser.setUser_birthday(formData.getUser_birthday());
        tempUser.setUser_addr(formData.getUser_addr());
        tempUser.setUser_addr_detail(formData.getUser_addr_detail());
        
        try {
            if (tempUser.getUser_no() == 0) {
                // [신규 회원 INSERT]
                kakaoService.socialJoin_withDetails(tempUser); 
                log.info("카카오 신규 회원 가입 완료: {}", tempUser.getUser_id());
            } else {
                // [기존 회원 UPDATE]
                kakaoService.socialUpdate_withDetails(tempUser);
                log.info("카카오 기존 회원 추가 정보 업데이트 완료: {}", tempUser.getUser_id());
            }

            session.removeAttribute("temp_kakao_user");
            
            // 로그인 세션 생성
            session.setAttribute("loginUser", tempUser);
            
            // 🔻🔻🔻 [수정됨] 헤더가 인식할 수 있도록 "USER"로 저장 🔻🔻🔻
            session.setAttribute("role", "USER");
            
            return "redirect:/mainpage";

        } catch (Exception e) {
            log.error("카카오 회원가입/업데이트 처리 중 오류 발생", e);
            rttr.addFlashAttribute("message", "정보 저장 중 오류가 발생했습니다.");
            return "redirect:/register_social";
        }
    }
}