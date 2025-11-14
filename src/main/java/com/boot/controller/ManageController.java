package com.boot.controller;

import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.service.ManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ManageController {

    private final ManageService manageService;

    @GetMapping("/user_manage")
    public String UserManagePage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String Role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(Role)) {
            redirectAttributes.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage"; // 메인페이지로 이동
        }


        List<Mypet_UserDTO> UserList = manageService.UserList();
        model.addAttribute("UserList", UserList);

        return "user_manage";
    }

    @GetMapping("/user_detail")
    public String UserViewPage(@RequestParam int user_no, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String Role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(Role)) {
            redirectAttributes.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage"; // 메인페이지로 이동
        }

        Mypet_UserDTO UserInfo = manageService.UserInfo(user_no);
        model.addAttribute("UserInfo", UserInfo);

        List<Mypet_PetDTO> PetList = manageService.PetList(user_no);
        model.addAttribute("PetList", PetList);

        return "user_detail";
    }



    @GetMapping("/veterinary_manage")
    public String VeterinaryManagePage() {

        return "veterinary_manage";
    }

    @GetMapping("/grooming_manage")
    public String GroomingManagePage() {

        return "grooming_manage";
    }
}
