package com.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/calculate") // 기본 URL 경로: /calculate
public class CalculatorController {

    @GetMapping
    public String showCalculator(Model model) { 
        return "calculate"; 
    }

    @PostMapping("/result")
    public String calculateResult(
            @RequestParam("animalType") String animalType,
            @RequestParam("bcsScore") int bcsScore,
            RedirectAttributes redirectAttributes) {

        String diagnosis = performDiagnosis(animalType, bcsScore);
        
        redirectAttributes.addFlashAttribute("resultDiagnosis", diagnosis);
        redirectAttributes.addFlashAttribute("resultAnimal", animalType);
        redirectAttributes.addFlashAttribute("resultBcs", bcsScore);

        return "redirect:/calculate";
    }

    private String performDiagnosis(String animalType, int bcsScore) {
        String animalName = animalType.equals("DOG") ? "강아지" : "고양이";
        
        // BCS 5점 척도 기준 진단 로직 (1, 2, 3, 4, 5 입력 가정)
        if (bcsScore <= 2) {
            // BCS 1 또는 2 (마름/저체중)
            return animalName + "는 현재 저체중 (BCS " + bcsScore + "/5)입니다. 영양 보충 및 수의사 상담이 필요해요.";
        } else if (bcsScore == 3) {
            // BCS 3 (이상적)
            return animalName + "는 이상적인 체중 (BCS " + bcsScore + "/5)입니다! 현재 상태를 잘 유지해주세요! 🐕❤️";
        } else if (bcsScore == 4) {
            // BCS 4 (과체중 위험)
            return animalName + "는 과체중 위험 (BCS " + bcsScore + "/5)입니다. 운동과 식단 조절이 필요해요.";
        } else if (bcsScore == 5) {
            // BCS 5 (비만)
            return animalName + "는 비만 (BCS " + bcsScore + "/5)입니다. 즉시 수의사와 상담하세요!";
        } else {
            // 예외 처리
            return "오류: 잘못된 BCS 점수(" + bcsScore + ")가 입력되었습니다. 1부터 5까지의 점수를 선택해주세요.";
        }
    }
}