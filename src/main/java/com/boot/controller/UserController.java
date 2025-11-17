package com.boot.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.dto.FindAccountDTO;
import com.boot.dto.Mypet_AdminDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.service.UserService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private UserService userService;
	
	//이메일 전송 객체[디펜던시에 추가됨]
	@Autowired
	private JavaMailSender mailSender;
	

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
     *     아이디 찾기 처리
     * ============================ */
    
    @RequestMapping("/findAccountOK")
	public String findAccountOK(@RequestParam("account_email") String email,
	                          @RequestParam("account_phone") String phone,
	                          @RequestParam HashMap<String, String> param,
	                          RedirectAttributes redirectAttributes) {

    	String phoneClean = phone.replace("-", "").trim();

        param.put("account_phone", phoneClean);
    	log.info(phoneClean);
    	
	    ArrayList<FindAccountDTO> dtos = userService.findAccount(param);

	    FindAccountDTO dbDto = dtos.get(0);
	    String dbPhoneClean = dbDto.getAccount_phone().replace("-", "");
        log.info(dbPhoneClean);
	    
	    if (dtos != null && !dtos.isEmpty()) {
//	        FindAccountDTO dbDto = dtos.get(0);
	        
	        

	        // 이메일, 전화번호 일치 여부 확인
	        if (phoneClean.equals(dbPhoneClean) && email.equals(dbDto.getAccount_email())) {
	            try {
	                // HTML 메일 생성
	                MimeMessage message = mailSender.createMimeMessage();
	                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	                helper.setFrom("carrepair3team@gmail.com");
	                helper.setTo(email);
	                helper.setSubject("[MY PET 동물병원] 회원님의 아이디 정보입니다.");

	                // HTML 본문
	                String htmlContent =
	                        """
	                        <html>
	                        <body style="font-family: '맑은 고딕', sans-serif; background-color:#f5f5f5; padding:20px;">
	                          <div style="max-width:600px; margin:auto; background-color:#fff; border-radius:10px; box-shadow:0 2px 8px rgba(0,0,0,0.1); padding:30px;">
	                            <h2 style="color:#0078d4;">MY PET 동물병원🐾</h2>
	                            <p>안녕하세요, <strong>MY PET 동물병원 입니다</strong></p>
	                            <p>회원님의 아이디 정보는 아래와 같습니다.</p>
	                            <hr style="border:none; border-top:1px solid #ddd; margin:20px 0;">
	                            <p style="font-size:18px;">🔑 <strong>아이디:</strong> <span style="color:#0078d4;">%s</span></p>
	                            <hr style="border:none; border-top:1px solid #ddd; margin:20px 0;">
	                            <p style="font-size:14px; color:#555;">본 메일은 MY PET 동물병원 아이디 찾기 요청으로 자동 발송되었습니다.</p>
	                            <p style="font-size:14px; color:#999;">© 2025 MY PET 동물병원. All rights reserved.</p>
	                          </div>
	                        </body>
	                        </html>
	                        """.formatted(dbDto.getAccount_id());

	                // true → HTML 허용
	                helper.setText(htmlContent, true);

	                // 메일 전송
	                mailSender.send(message);

	                
	                return "findOK";
	            } catch (MessagingException e) {
	                e.printStackTrace();
	                
	                redirectAttributes.addFlashAttribute("findFail", true);
	        	    return "redirect:/findAccount";
	            }
	        }
	    }
	    
	    redirectAttributes.addFlashAttribute("findFail", true);
	    return "redirect:/findAccount";
	}
    
    /* ============================
     *     비밀번호 찾기&변경 처리
     * ============================ */

    
    @RequestMapping("/findPwYn")
    public String findPwYn(
            @RequestParam HashMap<String, String> param,
            RedirectAttributes redirectAttributes) {

        ArrayList<FindAccountDTO> dtos = userService.findPW(param);

        String email = param.get("account_email");
        String phone = param.get("account_phone");
        String id = param.get("account_id");

        if (dtos != null && !dtos.isEmpty()) {
            FindAccountDTO dbDto = dtos.get(0);

            if (phone.equals(dbDto.getAccount_phone()) &&
                email.equals(dbDto.getAccount_email()) &&
                id.equals(dbDto.getAccount_id())) {

                try {
                    // 임시 비밀번호 생성
                    String tempPw = UUID.randomUUID().toString().substring(0, 10);

                    // 사용자/관리자 둘 중 해당하는 테이블 업데이트 시도
                    userService.updateUserPwd(id, tempPw);
                    userService.updateAdminPwd(id, tempPw);

                    // 메일 발송
                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                    helper.setFrom("carrepair3team@gmail.com");
                    helper.setTo(email);
                    helper.setSubject("[MY PET 동물병원] 임시 비밀번호 안내");

                    String htmlContent = """
                        <html>
					    <body style="font-family: Arial, sans-serif; background-color:#f9f9f9; padding:20px;">
					        <div style="max-width:600px; margin:auto; background:#ffffff; border-radius:10px; padding:30px; box-shadow:0 2px 8px rgba(0,0,0,0.1);">
					            <h2 style="color:#2C3E50; text-align:center;">🔐 임시 비밀번호 발급 안내</h2>
					            <p style="font-size:16px; color:#333;">
					                안녕하세요, <b>%s</b> 님.
					            </p>
					            <p style="font-size:16px; color:#333;">
					                요청하신 임시 비밀번호를 아래와 같이 발급해드렸습니다.<br>
					                로그인 후 반드시 비밀번호를 변경해주세요.
					            </p>
					            <div style="margin:20px 0; text-align:center;">
					                <div style="display:inline-block; background-color:#3498db; color:#fff; font-size:18px; padding:12px 24px; border-radius:8px;">
					                    임시 비밀번호: <b>%s</b>
					                </div>
					            </div>
					            <p style="color:#888; font-size:14px; text-align:center;">
					                ※ 본 메일은 발신 전용입니다. 문의사항은 홈페이지를 통해 접수해주세요.
					            </p>
					        </div>
					    </body>
					    </html>
                            """.formatted(id, tempPw);

                    helper.setText(htmlContent, true);
                    mailSender.send(message);

                    return "findOK";

                } catch (Exception e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("findFail", true);
                    return "redirect:/find_password";
                }
            }
        }

        redirectAttributes.addFlashAttribute("findFail", true);
        return "redirect:/find_password";
    }


    
    
}
