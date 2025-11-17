package com.boot.controller;

import com.boot.dto.GroomingResDTO;
import com.boot.dto.MedicalResDTO;
import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.service.ManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/veterinaryRes_manage")
    public String VeterinaryResManagePage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String Role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(Role)) {
            redirectAttributes.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage"; // 메인페이지로 이동
        }

        List<MedicalResDTO> VeterinaryResList = manageService.VeterinaryResList();
        model.addAttribute("VeterinaryResList", VeterinaryResList);

        return "veterinaryRes_manage";
    }

    @GetMapping("/groomingRes_manage")
    public String GroomingResManagePage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String Role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(Role)) {
            redirectAttributes.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage"; // 메인페이지로 이동
        }

        List<GroomingResDTO> GroomingResList = manageService.GroomingResList();
        model.addAttribute("GroomingResList", GroomingResList);

        return "groomingRes_manage";
    }

    @PostMapping("/confirmRes")
    public String confirmRes(@RequestParam("res_no") int resNo, @RequestParam("type") String type) {
        log.info(resNo + " @@ " + type);

        String tableName;
        String redirectPath;

        if ("veterinary".equals(type)) {
            tableName = "veterinary_res";
            redirectPath = "redirect:/veterinaryRes_manage";
        } else {
            tableName = "grooming_res";
            redirectPath = "redirect:/groomingRes_manage";
        }

        Map<String, Object> params = new HashMap<>();
        params.put("res_no", resNo);
        params.put("tableName", tableName);

        manageService.confirmRes(params);

        return redirectPath;
    }

    @PostMapping("/cancelRes")
    public String cancelRes(@RequestParam("res_no") int resNo, @RequestParam("type") String type, @RequestParam("cancel_reason") String cancelReason) {

        log.info(resNo + " @@ " + type + " @@ " + cancelReason);

        String tableName;
        String redirectPath;

        if ("veterinary".equals(type)) {
            tableName = "veterinary_res";
            redirectPath = "redirect:/veterinaryRes_manage";
        } else {
            tableName = "grooming_res";
            redirectPath = "redirect:/groomingRes_manage";
        }

        Map<String, Object> params = new HashMap<>();
        params.put("res_no", resNo);
        params.put("tableName", tableName);
        params.put("cancel_reason", cancelReason);

        manageService.cancelRes(params);

        return redirectPath;
    }

}
