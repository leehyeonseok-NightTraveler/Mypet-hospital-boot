package com.boot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ManageController {

    @GetMapping("/user_manage")
    public String UserManagePage() {

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
