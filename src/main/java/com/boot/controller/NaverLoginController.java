package com.boot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.dto.Mypet_UserDTO;
import com.boot.service.NaverLoginService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth") 
public class NaverLoginController {

    private final NaverLoginService naverLoginService; 

    /**
     * 1. 네이버 로그인 버튼 클릭 시
     * (JSP에서 <a href="/auth/naver/login">...</a> 으로 호출)
     */
    @GetMapping("/naver/login")
    public String naverLogin(HttpSession httpSession) {
        String naverAuthUrl = naverLoginService.getNaverLoginURL(httpSession);
        log.info("네이버 인증 페이지로 리다이렉트: {}", naverAuthUrl);
        return "redirect:" + naverAuthUrl;
    }

    /**
     * 2. 네이버 콜백 처리 (Kakao_Controller 로직과 동일하게 구현)
     */
    @GetMapping("/naver/callback")
    public String naverCallback(@RequestParam String code, @RequestParam String state, HttpSession session, RedirectAttributes rttr) {
        
        try {
            String accessToken = naverLoginService.getNaverAccessToken(code, state, session);
            Mypet_UserDTO userInfo = naverLoginService.getNaverUserInfo(accessToken);

            if (userInfo == null || userInfo.getSocial_id() == null) {
                 throw new Exception("네이버 사용자 정보 조회 실패");
            }

            Mypet_UserDTO loginUser = naverLoginService.findUserBySocialId(userInfo.getSocial_id());

            if (loginUser == null) {
            	session.setAttribute("temp_naver_user", userInfo);                
            	session.setAttribute("social_type", "naver");
                log.info("신규 네이버 회원. 추가 정보 입력 페이지로 이동.");
                return "redirect:/auth/naver/register_social_naver";
            } else {
                if (loginUser.getUser_phone() == null || loginUser.getUser_phone().isEmpty()) {
                	session.setAttribute("temp_naver_user", loginUser);
                    log.info("기존 회원(휴대폰 정보 없음). 추가 정보 입력 페이지로 이동.");
                    return "redirect:/auth/naver/register_social_naver";                    
                } else {
                    session.setAttribute("loginUser", loginUser);
                    session.setAttribute("role", "USER"); 
                    session.setAttribute("loginType", "naver");

                    log.info("기존 네이버 회원 로그인 성공. 세션 생성 완료: {}", loginUser.getUser_id());
                    return "redirect:/mainpage"; 
                }
            }
        } catch (Exception e) {
            log.error("네이버 콜백 처리 중 오류", e);
            rttr.addFlashAttribute("message", "네이버 로그인에 실패했습니다.");
            return "redirect:/login";
        }
    }
    @GetMapping("/naver/register_social_naver")
    public String showNaverRegisterForm(HttpSession session, Model model, RedirectAttributes rttr) {
        
        // ⭐️ 세션에서 "temp_naver_user"를 찾습니다.
        Mypet_UserDTO tempUser = (Mypet_UserDTO) session.getAttribute("temp_naver_user"); 
        
        if (tempUser == null) {
            rttr.addFlashAttribute("message", "네이버 로그인 세션이 만료되었습니다.");
            return "redirect:/login";
        }
        
        model.addAttribute("userDTO", tempUser);
        model.addAttribute("socialType", "naver");
        
        model.addAttribute("formAction", "/auth/naver/register_process");
        
        return "register_social"; 
    }

    /**
     * 4. 네이버 전용 추가 정보 폼 제출 처리 (POST)
     */
    @PostMapping("/naver/register_process")
    public String processNaverRegister(@ModelAttribute Mypet_UserDTO formData, HttpSession session, RedirectAttributes rttr) {
        
        // ⭐️ 세션에서 "temp_naver_user"를 찾습니다.
        Mypet_UserDTO tempUser = (Mypet_UserDTO) session.getAttribute("temp_naver_user");
        
        if (tempUser == null) {
            rttr.addFlashAttribute("message", "네이버 로그인 세션이 만료되었습니다.");
            return "redirect:/login";
        }
        
        // 폼데이터(formData)를 세션정보(tempUser)에 덮어쓰기
        tempUser.setUser_phone(formData.getUser_phone());
        tempUser.setUser_gender(formData.getUser_gender());
        tempUser.setUser_birthday(formData.getUser_birthday());
        tempUser.setUser_addr(formData.getUser_addr());
        tempUser.setUser_addr_detail(formData.getUser_addr_detail());
        
        try {
            // ⭐️ naverLoginService를 호출합니다.
            if (tempUser.getUser_no() == 0) {
                // [신규 회원 INSERT]
                naverLoginService.socialJoin_withDetails(tempUser); 
                log.info("네이버 신규 회원 가입 완료: {}", tempUser.getUser_id());
            } else {
                // [기존 회원 UPDATE]
                naverLoginService.socialUpdate_withDetails(tempUser);
                log.info("네이버 기존 회원 추가 정보 업데이트 완료: {}", tempUser.getUser_id());
            }

            session.removeAttribute("temp_naver_user");
            
            // DB에서 최신 정보(role 포함)를 다시 가져옵니다.
            Mypet_UserDTO finalUser = naverLoginService.findUserBySocialId(tempUser.getSocial_id());

            // 최종 로그인 세션 생성
            session.setAttribute("loginUser", finalUser);
            session.setAttribute("role", "USER"); 
            session.setAttribute("loginType", "naver");
            
            return "redirect:/mainpage";

        } catch (Exception e) {
            log.error("네이버 회원가입/업데이트 처리 중 오류 발생", e);
            rttr.addFlashAttribute("message", "정보 저장 중 오류가 발생했습니다.");
            return "redirect:/auth/naver/register_social_naver"; // ⭐️ 네이버 폼으로 다시 이동
        }
}
}