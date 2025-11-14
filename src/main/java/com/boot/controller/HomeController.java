package com.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() { return "mainpage"; }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String register() { return "register"; }

    @GetMapping("/mainpage")
    public String mainpage() { return "mainpage"; }

    @GetMapping("/hospital_info")
    public String hospitalInfo() { return "hospital_info"; }

    @GetMapping("/map")
    public String map() { return "map"; }

    @GetMapping("/find_password")
    public String findPassword() { return "find_password"; }

    @GetMapping("/reset_password")
    public String resetPassword() { return "reset_password"; }

    @GetMapping("/reservation")
    public String reservation() { return "reservation"; }

    @GetMapping("/grooming_appointment")
    public String groomingAppointment() { return "grooming_appointment"; }

    @GetMapping("/medical_appointment")
    public String medicalAppointment() { return "medical_appointment"; }

    @GetMapping("/pet_add")
    public String petAdd() { return "pet_add"; }
}
