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
    public String googleLogin(@RequestParam(required = false) String returnUrl, HttpSession session) {
        if (returnUrl != null && !returnUrl.isEmpty()) {
            session.setAttribute("login_return_url", returnUrl);
        }
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
             rttr.addFlashAttribute("message", "구글 로그인에 실패했습니다. (API 정보 조회 오류)");
             return "redirect:/login";
        }

        Mypet_UserDTO loginUser = googleDAO.findUserBySocialId(userInfo.getSocial_id());
        
        String returnUrl = (String) session.getAttribute("login_return_url");
        String redirectUrl = (returnUrl != null && !returnUrl.isEmpty()) ? returnUrl : "/mainpage";
        session.removeAttribute("login_return_url"); // 세션에서 삭제

        if (loginUser == null) {
            // [CASE 1: 신규 회원]
            session.setAttribute("temp_google_user", userInfo);
            log.info("신규 구글 회원. 추가 정보 입력 페이지로 이동.");
            return "redirect:/register_google?returnUrl=" + redirectUrl; 
            
        } else {
            // [CASE 2: 기존 회원]
            if (loginUser.getUser_phone() == null || loginUser.getUser_phone().isEmpty()) {
                session.setAttribute("temp_google_user", loginUser);
                log.info("기존 회원(휴대폰 정보 없음). 추가 정보 입력 페이지로 이동.");
                return "redirect:/register_google?returnUrl=" + redirectUrl;
            } else {
                // [CASE 2-2: 모든 정보가 있는 기존 회원]
                session.setAttribute("loginUser", loginUser);
                session.setAttribute("role", "USER");
                log.info("기존 구글 회원 로그인 성공. 세션 생성 완료: {}", loginUser.getUser_id());
                return "redirect:" + redirectUrl;
            }
        }
    }

    @GetMapping("/register_google")
    public String showGoogleRegisterForm(HttpSession session, Model model, RedirectAttributes rttr,
                                         @RequestParam(required = false) String returnUrl) {
        
        Mypet_UserDTO tempUser = (Mypet_UserDTO) session.getAttribute("temp_google_user");
        
        if (tempUser == null) {
            rttr.addFlashAttribute("message", "로그인 세션이 만료되었습니다.");
            return "redirect:/login";
        }
        
        model.addAttribute("userDTO", tempUser);
        model.addAttribute("socialType", "google"); // 🔻 JSP 구분을 위해 "google" 전달
        model.addAttribute("returnUrl", returnUrl); // 🔻 폼으로 returnUrl 전달
        
        // 🔻🔻🔻 [수정] 폼이 전송될 URL(/register_google_process)을 모델에 추가 🔻🔻🔻
        model.addAttribute("formAction", "/register_google_process");
        
        return "register_social"; // 👈 공통 JSP 호출
    }

    @PostMapping("/register_google_process")
    public String processGoogleRegister(@ModelAttribute Mypet_UserDTO formData, HttpSession session, RedirectAttributes rttr,
                                        @RequestParam(required = false) String returnUrl) {
        
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
            
            // 🔻 폼에서 받은 returnUrl이 있으면 거기로, 없으면 /mainpage로 🔻
            String redirectUrl = (returnUrl != null && !returnUrl.isEmpty()) ? returnUrl : "/mainpage";
            return "redirect:" + redirectUrl;

        } catch (Exception e) {
            log.error("구글 회원가입/업데이트 처리 중 오류 발생", e);
            rttr.addFlashAttribute("message", "정보 저장 중 오류가 발생했습니다.");
            // 🔻 폼으로 다시 돌려보낼 때 returnUrl을 유지
            return "redirect:/register_google?returnUrl=" + returnUrl; 
        }
    }
}