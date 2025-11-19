package com.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CalendarController {
	// 1. 진료 내역 캘린더 페이지 이동
    @GetMapping("/mypage_medical")
    public String showMedicalCalendar() {
        return "mypage_medical";
    }

    // 2. 미용 내역 캘린더 페이지 이동
    @GetMapping("/mypage_grooming")
    public String showGroomingCalendar() {
        return "mypage_grooming"; 
    }
}
