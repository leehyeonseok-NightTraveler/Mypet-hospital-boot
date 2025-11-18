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
import com.boot.service.Mypet_GoogleService;
import com.boot.dao.Mypet_Google_DAO; 

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class Google_Controller {

    private final Mypet_GoogleService googleService;
    private final Mypet_Google_DAO googleDAO; 

    // 1. "구글 로그인" 버튼 클릭 시
    @GetMapping("/auth/google/login")
    public String googleLogin() {
        String googleAuthUrl = googleService.getGoogleLoginURL();
        return "redirect:" + googleAuthUrl;
    }

    /**
     * 2. 구글 콜백 처리 (DB 검사 로직 포함)
     */
    @GetMapping("/auth/google/callback")
    public String googleCallback(@RequestParam String code, HttpSession session, RedirectAttributes rttr) {
        
        Mypet_UserDTO userInfo = googleService.getGoogleUserInfo(googleService.getGoogleAccessToken(code));
        
        if (userInfo == null || userInfo.getSocial_id() == null) {
             rttr.addFlashAttribute("message", "구글 로그인에 실패했습니다.");
             return "redirect:/login";
        }

        Mypet_UserDTO loginUser = googleDAO.findUserBySocialId(userInfo.getSocial_id());

        if (loginUser == null) {
            // [CASE 1: 신규 회원]
            session.setAttribute("temp_google_user", userInfo); // 🔻 구글 전용 세션
            log.info("신규 구글 회원. 추가 정보 입력 페이지로 이동.");
            return "redirect:/register_social_google"; // 👈 구글 전용 GET
            
        } else {
            // [CASE 2: 기존 회원]
            if (loginUser.getUser_phone() == null || loginUser.getUser_phone().isEmpty()) {
                session.setAttribute("temp_google_user", loginUser); // 🔻 구글 전용 세션
                log.info("기존 회원(휴대폰 정보 없음). 추가 정보 입력 페이지로 이동.");
                return "redirect:/register_social_google"; // 👈 구글 전용 GET
            } else {
                session.setAttribute("loginUser", loginUser);
                session.setAttribute("role", "USER");
                log.info("기존 구글 회원 로그인 성공. 세션 생성 완료: {}", loginUser.getUser_id());
                return "redirect:/mainpage";
            }
        }
    }

    /**
     * 3. 🔻 구글 전용 추가 정보 입력 폼 (GET) 🔻
     */
    @GetMapping("/register_social_google")
    public String showSocialRegisterForm(HttpSession session, Model model, RedirectAttributes rttr) {
        
        Mypet_UserDTO tempUser = (Mypet_UserDTO) session.getAttribute("temp_google_user");
        
        if (tempUser == null) {
            rttr.addFlashAttribute("message", "로그인 세션이 만료되었습니다.");
            return "redirect:/login";
        }
        
        model.addAttribute("userDTO", tempUser);
        model.addAttribute("socialType", "google"); // 🔻 JSP 구분을 위해 "google" 전달
        
        return "register_social"; // 👈 공통 JSP 호출
    }

    /**
     * 4. 🔻 구글 전용 폼 처리 (POST) 🔻
     */
    @PostMapping("/register_social_google_process")
    public String processGoogleRegister(@ModelAttribute Mypet_UserDTO formData, HttpSession session, RedirectAttributes rttr) {
        
        Mypet_UserDTO tempUser = (Mypet_UserDTO) session.getAttribute("temp_google_user");
        
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
            // 주소 합치기 및 상태 설정
            String fullAddress = "(" + tempUser.getUser_addr() + ") " + tempUser.getUser_addr_detail();
            tempUser.setUser_addr(fullAddress);
            tempUser.setUser_status("ACTIVE");

            // 🔻 구글 DAO로 DB 저장/업데이트 🔻
            if (tempUser.getUser_no() == 0) {
                googleDAO.socialJoin_withDetails(tempUser); 
            } else {
                googleDAO.socialUpdate_withDetails(tempUser);
            }

            session.removeAttribute("temp_google_user");
            session.setAttribute("loginUser", tempUser);
            session.setAttribute("role", "USER");
            return "redirect:/mainpage";

        } catch (Exception e) {
            log.error("구글 회원가입/업데이트 처리 중 오류 발생", e);
            rttr.addFlashAttribute("message", "정보 저장 중 오류가 발생했습니다.");
            return "redirect:/register_social_google"; // 👈 구글 전용 GET
        }
    }
}