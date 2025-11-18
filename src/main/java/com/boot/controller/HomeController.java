package com.boot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

//    아이디 찾기
    @RequestMapping("/findAccount")
    public String find_account() { return "findAccount"; }
    
    @GetMapping("/find_password")
    public String findPassword() { return "find_password"; }
    
//    아이디/비밀번호 찾기 완료 페이지
	@RequestMapping("/findOK")
	public String infdOK() { return "findOK"; }

    @GetMapping("/reservation")
    public String reservation() { return "reservation"; }

    @GetMapping("/grooming_appointment")
    public String groomingAppointment() { return "grooming_appointment"; }

    @GetMapping("/medical_appointment")
    public String medicalAppointment() { return "medical_appointment"; }

    @GetMapping("/pet_add")
    public String petAdd() { return "pet_add"; }
    
	@RequestMapping("/community_write_view")
	public String community_write_view(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {return "login";}
		return "community_write_view";}
	
}
