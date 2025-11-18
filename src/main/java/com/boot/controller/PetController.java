package com.boot.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.dao.PetDAO;
import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.dto.PetWeightDTO;
import com.boot.service.PetService;
import com.boot.service.UploadService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final UploadService uploadService;
    
    @GetMapping("/mypage_petinfo")
    public String mypagePetInfo(
            HttpServletRequest request,
            Model model,
            HttpSession session
    ) 
    {
        int pet_no = Integer.parseInt(request.getParameter("pet_no"));

        
        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        Mypet_PetDTO petInfo = petService.getPetDetails(pet_no);
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

            // ⭐ 추가!
            @RequestParam(value = "pet_img", required = false) MultipartFile pet_img,

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

        // 생일 처리
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

        try {
            // ⭐ 신규 이미지 파일이 있으면 저장
            if (pet_img != null && !pet_img.isEmpty()) {

                String saved = uploadService.saveImageWithHash(pet_img, "pet");
                petDTO.setPet_img(saved);

                // 해시값 저장
                String fileName = saved.substring(saved.lastIndexOf("/") + 1);
                String hash = fileName.split("_")[1];
                petDTO.setPet_img_temp(hash);
            }

            petService.petjoin(petDTO);

            log.info("펫 등록 완료(이미지 적용): {} / img={}", petName, petDTO.getPet_img());

        } catch (Exception e) {
            log.error("펫 등록 오류", e);
            return "errorPage";
        }

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
            @RequestParam(value = "pet_img", required = false) MultipartFile pet_img,
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
            
            // 이미지 파일 있을 때만 업로드 적용
            if (pet_img != null && !pet_img.isEmpty()) {
                petService.replacePetImage(pet_no, pet_img);

                log.info("펫 이미지 변경 완료: pet_no={}", pet_no);
            }
            
            log.info("펫 정보 수정 완료: {} (user_no={})", pet_name, loginUser.getUser_no());
        } catch (Exception e) {
            log.error("펫 정보 수정 중 오류 발생", e);
            ra.addFlashAttribute("message", "수정 중 오류 발생: " + e.getMessage());
            return "redirect:/mypage_petinfo_edit?pet_no=" + pet_no;
        }

        ra.addFlashAttribute("message", "펫 정보가 성공적으로 수정되었습니다!");
        return "redirect:/mypage_petinfo?pet_no=" + pet_no;
    }
    
    @PostMapping("/pet/uploadImg")
    public ResponseEntity<String> uploadPetImg(
            @RequestParam("file") MultipartFile file,
            @RequestParam("petNo") int petNo) {

        try {
            petService.replacePetImage(petNo, file);
            return ResponseEntity.ok("success");

        } catch (Exception e) {
            log.error("펫 이미지 업로드 실패", e);
            return ResponseEntity.status(500).body("fail");
        }
    }

    @GetMapping("/mypage/pet/{petNo}")
    public String petDetailPage(@PathVariable int petNo, Model model) {
        // ServiceImpl의 getPetDetails가 DB에서 펫 정보와 현재/권장 체중을 가져옵니다.
        Mypet_PetDTO petInfo = petService.getPetDetails(petNo);
        
        model.addAttribute("petInfo", petInfo);
        return "mypage_petinfo"; // ⭐️ /WEB-INF/views/mypage_pet_detail.jsp 파일을 엽니다.
    }

    /**
     * 2. 체중 기록 추가 (폼 전송 처리)
     */
    @PostMapping("/mypage/pet/{petNo}/add_weight")
    public String addWeight(@PathVariable int petNo, @RequestParam double weight_kg) {
        // ServiceImpl의 addPetWeight가 DB에 INSERT합니다.
        petService.addPetWeight(petNo, weight_kg);
        
        // 저장이 완료되면, 다시 상세 정보 페이지로 돌아갑니다.
        return "redirect:/mypage_petinfo?pet_no=" + petNo;
    }

    /**
     * 3. ⭐️ 그래프 데이터 전용 (JSON 반환)
     * (JSP의 JavaScript(AJAX)가 이 URL을 호출합니다)
     */
    @GetMapping("/api/pet/{petNo}/weight_history")
    @ResponseBody // ⭐️ JSP 파일이 아닌, JSON 데이터 자체를 반환합니다.
    public List<PetWeightDTO> getWeightChartData(@PathVariable int petNo) {
        // ServiceImpl의 getWeightHistory가 DB에서 체중 기록 목록을 가져옵니다.
        return petService.getWeightHistory(petNo);
    }

}
