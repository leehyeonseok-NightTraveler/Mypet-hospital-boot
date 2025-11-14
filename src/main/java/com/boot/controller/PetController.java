package com.boot.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.service.PetService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping("/mypage_petinfo")
    public String mypagePetInfo(
            HttpServletRequest request,
            Model model,
            HttpSession session
    ) {
        int pet_no = Integer.parseInt(request.getParameter("pet_no"));

        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        Mypet_PetDTO petInfo = petService.getPetInfo(loginUser.getUser_no(), pet_no);
        model.addAttribute("petInfo", petInfo);

        return "mypage_petinfo";
    }

    @PostMapping("/petjoinProcess")
    public String petjoinProcess(
            @RequestParam("pet_name") String petName,
            @RequestParam("pet_species") String petSpecies,
            @RequestParam("pet_breed") String petBreed,
            @RequestParam("pet_gender") String petGender,
            @RequestParam("pet_birthday") String petBirthdayStr,
            @RequestParam("pet_neutered") String petNeutered,
            HttpSession session
    ) {

        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        Mypet_PetDTO petDTO = new Mypet_PetDTO();
        petDTO.setUser_no(loginUser.getUser_no());
        petDTO.setPet_name(petName);
        petDTO.setPet_species(petSpecies);
        petDTO.setPet_breed(petBreed);
        petDTO.setPet_gender(petGender);
        petDTO.setPet_neutered(petNeutered);

        try {
            if (petBirthdayStr != null && !petBirthdayStr.isEmpty()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = format.parse(petBirthdayStr);
                petDTO.setPet_birthday(new java.sql.Date(utilDate.getTime()));
            }
        } catch (Exception e) {
            log.error("펫 생일 변환 오류", e);
            return "errorPage";
        }

        petService.petjoin(petDTO);
        log.info("펫 등록 완료: {} (user_no={})", petName, loginUser.getUser_no());

        return "redirect:/mypage_petlist";
    }

    @GetMapping("/mypage_petlist")
    public String mypagePetList(
            HttpSession session,
            Model model
    ) {
        log.info(" [Controller] /mypage_petlist 진입 성공");

        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        List<Mypet_PetDTO> petList = petService.getPetsByUserNo(loginUser.getUser_no());
        model.addAttribute("petList", petList);
        model.addAttribute("loginUser", loginUser);

        loginUser.setPets(petList);

        log.info("펫 목록 조회: {} (총 {}마리)",
                loginUser.getUser_id(),
                petList != null ? petList.size() : 0);

        return "mypage_petlist";
    }

    @GetMapping("/mypage_petinfo_edit")
    public String mypagePetInfoEdit(
            @RequestParam("pet_no") int pet_no,
            HttpSession session,
            Model model
    ) {
        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        Mypet_PetDTO petInfo = petService.getPetInfo(loginUser.getUser_no(), pet_no);
        if (petInfo == null) return "redirect:/mypage_petlist";

        model.addAttribute("petInfo", petInfo);
        return "mypage_petinfo_edit";
    }

    @PostMapping("/mypage_petinfo_edit_ok")
    public String mypagePetInfoEditOk(
            @RequestParam("pet_no") int pet_no,
            @RequestParam("pet_name") String pet_name,
            @RequestParam("pet_age") int pet_age,
            @RequestParam("pet_birthday") String pet_birthday,
            @RequestParam("pet_gender") String pet_gender,
            @RequestParam("pet_species") String pet_species,
            @RequestParam("pet_breed") String pet_breed,
            HttpSession session,
            RedirectAttributes ra
    ) {
        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_no", loginUser.getUser_no());
        map.put("pet_no", pet_no);
        map.put("pet_name", pet_name);
        map.put("pet_age", pet_age);
        map.put("pet_birthday", pet_birthday);
        map.put("pet_gender", pet_gender);
        map.put("pet_species", pet_species);
        map.put("pet_breed", pet_breed);

        try {
            petService.updatePetInfo(map);
            log.info("펫 정보 수정 완료: {} (user_no={})", pet_name, loginUser.getUser_no());
        } catch (Exception e) {
            log.error("펫 정보 수정 중 오류 발생", e);
            ra.addFlashAttribute("message", "수정 중 오류 발생: " + e.getMessage());
            return "redirect:/mypage_petinfo_edit?pet_no=" + pet_no;
        }

        ra.addFlashAttribute("message", "펫 정보가 성공적으로 수정되었습니다!");
        return "redirect:/mypage_petinfo?pet_no=" + pet_no;
    }
}
