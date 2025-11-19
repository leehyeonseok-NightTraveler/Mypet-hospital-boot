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
import com.boot.dao.Mypet_Kakao_DAO; // 🔻 카카오 DAO Import

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class Kakao_Controller {

    private final Mypet_KakaoService kakaoService;
    private final Mypet_Kakao_DAO kakaoDAO; // 🔻 카카오 DAO 주입

    // 1. "카카오 로그인" 버튼 클릭 시
    @GetMapping("/auth/kakao/login")
    public String kakaoLogin(@RequestParam(required = false) String returnUrl, HttpSession session) { // 🔻 [수정] returnUrl 받기
        // 🔻 [수정] returnUrl이 있으면 세션에 저장
        if (returnUrl != null && !returnUrl.isEmpty()) {
            session.setAttribute("login_return_url", returnUrl);
        }
        String kakaoAuthUrl = kakaoService.getKakaoLoginURL();
        return "redirect:" + kakaoAuthUrl;
    }

    // 2. 카카오 콜백 처리
    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam String code, HttpSession session, RedirectAttributes rttr) {
        
        Mypet_UserDTO userInfo = kakaoService.getKakaoUserInfo(kakaoService.getKakaoAccessToken(code));
        
        if (userInfo == null || userInfo.getSocial_id() == null) {
             rttr.addFlashAttribute("message", "카카오 로그인에 실패했습니다.");
             return "redirect:/login";
        }

        Mypet_UserDTO loginUser = kakaoDAO.findUserBySocialId(userInfo.getSocial_id());

        // 🔻 [수정] returnUrl 로직 추가
        String returnUrl = (String) session.getAttribute("login_return_url");
        String redirectUrl = (returnUrl != null && !returnUrl.isEmpty()) ? returnUrl : "/mainpage";
        session.removeAttribute("login_return_url"); // 세션에서 삭제

        if (loginUser == null) {
            // [CASE 1: 신규 회원]
            session.setAttribute("temp_kakao_user", userInfo); // 🔻 카카오 전용 세션
            log.info("신규 카카오 회원. 추가 정보 입력 페이지로 이동.");
            // 🔻 [수정] 리다이렉트 시 returnUrl 전달
            return "redirect:/register_social_kakao?returnUrl=" + redirectUrl; 
            
        } else {
            // [CASE 2: 기존 회원]
            if (loginUser.getUser_phone() == null || loginUser.getUser_phone().isEmpty()) {
                session.setAttribute("temp_kakao_user", loginUser); // 🔻 카카오 전용 세션
                log.info("기존 회원(휴대폰 정보 없음). 추가 정보 입력 페이지로 이동.");
                // 🔻 [수정] 리다이렉트 시 returnUrl 전달
                return "redirect:/register_social_kakao?returnUrl=" + redirectUrl; 
            } else {
                session.setAttribute("loginUser", loginUser);
                session.setAttribute("role", "USER"); 
                log.info("기존 카카오 회원 로그인 성공. 세션 생성 완료: {}", loginUser.getUser_id());
                // 🔻 [수정] 최종 목적지로 리다이렉트
                return "redirect:" + redirectUrl;
            }
        }
    }

    /**
     * 3. 🔻 카카오 전용 추가 정보 입력 폼 (GET) 🔻
     */
    @GetMapping("/register_social_kakao")
    public String showSocialRegisterForm(HttpSession session, Model model, RedirectAttributes rttr,
                                         @RequestParam(required = false) String returnUrl) { // 🔻 [수정] returnUrl 받기
        
        Mypet_UserDTO tempUser = (Mypet_UserDTO) session.getAttribute("temp_kakao_user");
        
        if (tempUser == null) {
            rttr.addFlashAttribute("message", "카카오 로그인 세션이 만료되었습니다.");
            return "redirect:/login";
        }
        
        model.addAttribute("userDTO", tempUser);
        model.addAttribute("socialType", "kakao");
        model.addAttribute("returnUrl", returnUrl); // 🔻 [수정] 모델에 returnUrl 추가 (JSP hidden input용)
        
        // 🔻🔻🔻 [핵심 수정] 404 오류 수정 🔻🔻🔻
        model.addAttribute("formAction", "/register_social_kakao_process"); // 👈 PostMapping 주소와 일치
        
        return "register_social";
    }

    /**
     * 4. 🔻 카카오 전용 폼 처리 (POST) 🔻
     */
    @PostMapping("/register_social_kakao_process")
    public String processSocialRegister(@ModelAttribute Mypet_UserDTO formData, HttpSession session, RedirectAttributes rttr,
                                        @RequestParam(required = false) String returnUrl) { // 🔻 [수정] returnUrl 받기
        
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
            // 주소 합치기 및 상태 설정
            String fullAddress = "(" + tempUser.getUser_addr() + ") " + tempUser.getUser_addr_detail();
            tempUser.setUser_addr(fullAddress);
            tempUser.setUser_status("ACTIVE");

            // 🔻 카카오 DAO로 DB 저장/업데이트 🔻
            if (tempUser.getUser_no() == 0) {
                kakaoDAO.socialJoin_withDetails(tempUser); 
            } else {
                kakaoDAO.socialUpdate_withDetails(tempUser);
            }

            session.removeAttribute("temp_kakao_user");
            session.setAttribute("loginUser", tempUser);
            session.setAttribute("role", "USER");
            
            // 🔻 [수정] 폼에서 받은 returnUrl이 있으면 거기로, 없으면 /mainpage로
            String redirectUrl = (returnUrl != null && !returnUrl.isEmpty()) ? returnUrl : "/mainpage";
            return "redirect:" + redirectUrl;

        } catch (Exception e) {
            log.error("카카오 회원가입/업데이트 처리 중 오류 발생", e);
            rttr.addFlashAttribute("message", "정보 저장 중 오류가 발생했습니다.");
            // 🔻 [수정] 폼으로 다시 돌려보낼 때 returnUrl을 유지
            return "redirect:/register_social_kakao?returnUrl=" + returnUrl; 
        }
    }
}