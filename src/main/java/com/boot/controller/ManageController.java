package com.boot.controller;

import com.boot.dto.Mypet_UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ManageController {

    @GetMapping("/user_manage")
    public String UserManagePage(HttpSession session) {
        String Role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(Role)) {
            return "login";
        }

        return "user_manage";
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
