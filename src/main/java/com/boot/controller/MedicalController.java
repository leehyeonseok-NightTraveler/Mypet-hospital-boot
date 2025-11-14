package com.boot.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.dto.MedicalResDTO;
import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.service.MedicalService;
import com.boot.service.PetService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MedicalController {

    private final MedicalService medicalService;
    private final PetService petService;


    /* ============================
     *     진료 예약 조회 페이지
     * ============================ */
    @GetMapping("/reservation_pet_medical_check")
    public String showMedicalCheckPage() {
        return "reservation_pet_medical_check";
    }


    /* ============================
     *     진료 예약 조회 처리
     * ============================ */
    @PostMapping("/reservation_pet_medical_reference")
    public String handleMedicalReference(
            @RequestParam("userName") String userName,
            @RequestParam("phone1") String phone1,
            @RequestParam("phone2") String phone2,
            @RequestParam("phone3") String phone3,
            Model model
    ) {

        String fullPhoneNumber = phone1 + "-" + phone2 + "-" + phone3;

        HashMap<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("userPhone", fullPhoneNumber);

        List<MedicalResDTO> reservationList = medicalService.findMedicalReservations(map);

        if (reservationList != null && !reservationList.isEmpty()) {
            model.addAttribute("reservationList", reservationList);
        } else {
            model.addAttribute("errorMessage", "일치하는 예약 내역이 없습니다.");
        }

        return "reservation_pet_medical_reference";
    }


    /* ============================
     *     진료 예약 작성 페이지
     * ============================ */
    @GetMapping("/reservation_pet_medical")
    public String showMedicalForm(HttpSession session, Model model) {

        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        // 🔥 PetService 사용
        List<Mypet_PetDTO> petList = petService.getPetsByUserNo(loginUser.getUser_no());
        model.addAttribute("petList", petList);

        return "reservation_pet_medical";
    }


    /* ============================
     *     진료 예약 생성 처리
     * ============================ */
    @PostMapping("/reservation/medical/create")
    public String createMedicalReservation(
            MedicalResDTO dto,
            @RequestParam("phone2") String phone2,
            @RequestParam("phone3") String phone3,
            HttpSession session,
            RedirectAttributes rttr
    ) {

        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        dto.setUser_no(loginUser.getUser_no());
        dto.setUser_phone("010-" + phone2 + "-" + phone3);

        // 🔥 PetService 사용해야 함
        Mypet_PetDTO selectedPet = petService.getPetByNo(dto.getPet_no());
        if (selectedPet != null) {
            dto.setPet_name(selectedPet.getPet_name());
        }

        // DB 저장
        medicalService.createMedicalReservation(dto);

        rttr.addFlashAttribute("successMessage", "예약이 성공적으로 접수되었습니다!");
        return "redirect:/reservation_pet_medical_check";
    }
}
