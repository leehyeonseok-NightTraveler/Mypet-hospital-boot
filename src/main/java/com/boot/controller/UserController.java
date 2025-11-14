package com.boot.controller;

import java.sql.Date;
import java.util.HashMap;
import javax.servlet.http.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.dto.Mypet_AdminDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.service.UserService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* ============================
     *          회원가입
     * ============================ */
    @PostMapping("/registerProcess")
    public String registerProcess(
            MultipartHttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Mypet_UserDTO dto = new Mypet_UserDTO();

        dto.setUser_id(request.getParameter("user_id"));
        dto.setUser_pwd(request.getParameter("user_pwd"));
        dto.setUser_name(request.getParameter("user_name"));
        dto.setUser_gender(request.getParameter("user_gender"));

        String birthday = request.getParameter("user_birthday");
        if (birthday != null && !birthday.isEmpty()) {
            dto.setUser_birthday(Date.valueOf(birthday));
        }

        dto.setUser_phone(request.getParameter("user_phone"));
        dto.setUser_email(request.getParameter("user_email"));

        String addr = request.getParameter("user_addr");
        String addrDetail = request.getParameter("user_addr_detail");
        dto.setUser_addr((addr != null ? addr : "") + " " + (addrDetail != null ? addrDetail : ""));

        dto.setUser_status("ACTIVE");

        userService.join(dto);
        log.info("회원가입 완료: {}", dto.getUser_id());

        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        return "redirect:/login";
    }


    /* ============================
     *            로그인
     * ============================ */
    @PostMapping("/loginProcess")
    public String loginProcess(
            HttpServletRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        String user_id = request.getParameter("user_id");
        String user_pwd = request.getParameter("user_pwd");

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("user_pwd", user_pwd);

        Object loginObj = userService.login(map);

        if (loginObj instanceof Mypet_AdminDTO) {
            session.setAttribute("role", "ADMIN");
            session.setAttribute("loginAdmin", loginObj);
            log.info("관리자 로그인 성공: {}", user_id);
            return "mainpage";
        }

        if (loginObj instanceof Mypet_UserDTO) {
            Mypet_UserDTO user = (Mypet_UserDTO) loginObj;
            session.setAttribute("role", "USER");
            session.setAttribute("loginUser", user);
            log.info("일반 사용자 로그인 성공: {}", user_id);
            return "mainpage";
        }

        redirectAttributes.addFlashAttribute("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
        return "redirect:/login";
    }


    /* ============================
     *           로그아웃
     * ============================ */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        log.info("로그아웃 완료");
        return "redirect:/login";
    }


    /* ============================
     *        마이페이지
     * ============================ */
    @GetMapping("/mypage_userinfo")
    public String mypageUserInfo(HttpSession session, Model model) {

        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        model.addAttribute("loginUser", loginUser);
        return "mypage_userinfo";
    }

    /* ============================
     *   마이페이지 수정 화면 이동
     * ============================ */
    @GetMapping("/mypage_userinfo_edit")
    public String mypageUserInfoEdit(HttpSession session, Model model) {
        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        model.addAttribute("loginUser", loginUser);
        return "mypage_userinfo_edit";
    }


    /* ============================
     *     회원 정보 수정 처리
     * ============================ */
    @PostMapping("/mypage_userinfo_edit_ok")
    public String mypageUserInfoEditOk(
            @RequestParam(value = "user_pwd", required = false) String user_pwd,
            @RequestParam(value = "user_pwd_confirm", required = false) String user_pwd_confirm,
            @RequestParam("user_phone") String user_phone,
            @RequestParam("user_email") String user_email,
            @RequestParam(value = "user_addr", required = false) String user_addr,
            @RequestParam(value = "user_addr_detail", required = false) String user_addr_detail,
            @RequestParam(value = "user_img", required = false) MultipartFile user_img,
            HttpSession session,
            RedirectAttributes ra
    ) {

        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            ra.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_no", loginUser.getUser_no());
        map.put("user_phone", user_phone);
        map.put("user_email", user_email);
        map.put("user_addr", (user_addr != null ? user_addr : "") + " " + (user_addr_detail != null ? user_addr_detail : ""));

        if (user_pwd != null && !user_pwd.isEmpty()) {
            if (!user_pwd.equals(user_pwd_confirm)) {
                ra.addFlashAttribute("message", "비밀번호 확인이 일치하지 않습니다.");
                return "redirect:/mypage_userinfo_edit";
            }
            map.put("user_pwd", user_pwd);
        }

        try {
            userService.updateUserInfo(map);

            // DB 기준 세션 갱신
            Mypet_UserDTO updatedUser = userService.getUserByNo(loginUser.getUser_no());
            if (updatedUser != null) {
                session.setAttribute("loginUser", updatedUser);
            } else {
                loginUser.setUser_phone(user_phone);
                loginUser.setUser_email(user_email);
                loginUser.setUser_addr(user_addr);
                if (user_pwd != null && !user_pwd.isEmpty()) {
                    loginUser.setUser_pwd(user_pwd);
                }
                session.setAttribute("loginUser", loginUser);
            }

            log.info("회원 정보 수정 완료: user_no={}", loginUser.getUser_no());
            ra.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다!");

        } catch (Exception e) {
            log.error("회원 정보 수정 중 오류 발생", e);
            ra.addFlashAttribute("message", "수정 중 오류 발생: " + e.getMessage());
        }

        return "redirect:/mypage_userinfo";
    }
    
    
    /* ============================
     *     비밀번호 찾기 처리
     * ============================ */
    @PostMapping("/findPasswordProcess")
    public String findPasswordProcess(
            HttpServletRequest request,
            Model model,
            RedirectAttributes rttr
    ) {
        String user_id = request.getParameter("user_id");
        String user_name = request.getParameter("user_name");
        String user_email = request.getParameter("user_email");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("user_name", user_name);
        map.put("user_email", user_email);

        if (userService.checkUserExists(map)) {
            model.addAttribute("user_id", user_id);
            return "reset_password";
        } else {
            rttr.addFlashAttribute("error", "일치하는 회원 정보가 없습니다.");
            return "redirect:/findPassword";
        }
    }


    /* ============================
     *     비밀번호 변경 처리
     * ============================ */
    @PostMapping("/updatePassword")
    public String updatePassword(
            HttpServletRequest request,
            RedirectAttributes rttr
    ) {
        String user_id = request.getParameter("user_id");
        String user_pwd = request.getParameter("user_pwd");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("user_pwd", user_pwd);

        if (userService.updatePassword(map)) {
            rttr.addFlashAttribute("success", "비밀번호가 성공적으로 변경되었습니다.");
            log.info("비밀번호 변경 완료: {}", user_id);
            return "redirect:/login";
        } else {
            rttr.addFlashAttribute("error", "비밀번호 변경 중 오류가 발생했습니다.");
            return "redirect:/findPassword";
        }
    }

    
    
}
