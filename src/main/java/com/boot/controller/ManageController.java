package com.boot.controller;

import com.boot.dto.*;
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

    /**
     * 회원 목록 관리 페이지를 처리합니다.
     * 관리자 권한을 확인하고, 페이징된 회원 목록을 모델에 담아 전달합니다.
     *
     * @param cri 페이징/검색 조건을 담는 Criteria 객체
     */
    @GetMapping("/user_manage")
    public String UserManagePage(@RequestParam(value = "status", required = false) String status,
                                 Criteria cri, HttpSession session, Model model,
                                 RedirectAttributes rttr) {
        String Role = (String) session.getAttribute("role");

        // 관리자 권한 확인
        if (!"ADMIN".equals(Role)) {
            rttr.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage";
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", status); // 상태 필터
        params.put("keyword", cri.getKeyword());

        // 전체 회원 수 조회 및 PageDTO 생성 (페이징 정보)
        int total = manageService.getUserTotal(params, cri);
        model.addAttribute("pageMaker", new PageDTO(total, cri));

        // 페이징된 회원 목록 조회
        List<Mypet_UserDTO> UserList = manageService.UserList(params, cri);
        model.addAttribute("UserList", UserList);

        return "user_manage";
    }

    /**
     * 특정 회원의 상세 정보 페이지를 처리합니다.
     * 관리자 권한을 확인하고, 회원 정보와 반려동물 목록, 그리고 페이징 정보(cri)를 모델에 담아 전달합니다.
     *
     * @param user_no 조회할 회원 번호
     * @param cri     목록 복귀를 위한 페이징/검색 조건
     */
    @GetMapping("/user_detail")
    public String UserViewPage(@RequestParam int user_no, Criteria cri,
                               HttpSession session, Model model,
                               RedirectAttributes rttr) {
        String Role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(Role)) {
            rttr.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage";
        }

        // 회원 정보
        Mypet_UserDTO UserInfo = manageService.UserInfo(user_no);
        model.addAttribute("UserInfo", UserInfo);

        // 펫 목록
        List<Mypet_PetDTO> PetList = manageService.PetList(user_no);
        model.addAttribute("PetList", PetList);

        // ★ 멤버십 등급 이력 추가
        model.addAttribute("GradeHistory", manageService.getGradeHistory(user_no));

        // ★ 멤버십 이용(방문) 이력 추가
        model.addAttribute("ServiceHistory", manageService.getServiceHistory(user_no));

        model.addAttribute("cri", cri);

        return "user_detail";
    }


    @GetMapping("/user_servicehistory")
    public String userServiceHistoryForm(@RequestParam("user_no") int userNo,
                                         Criteria cri,
                                         HttpSession session,
                                         Model model,
                                         RedirectAttributes rttr) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            rttr.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage";
        }

        // 1) 유저 정보 기본 전달
        model.addAttribute("user_no", userNo);
        model.addAttribute("cri", cri);

        // 2) 펫 리스트 조회
        List<Mypet_PetDTO> petList = manageService.getPetList(userNo);

        // 3) JSP 전달
        model.addAttribute("PetList", petList);

        return "user_servicehistory";
    }


    @PostMapping("/user_servicehistoryProcess")
    public String userServiceHistoryProcess(ServiceHistoryDTO dto,
                                            @RequestParam int pageNum,
                                            @RequestParam int amount,
                                            RedirectAttributes rttr) {

        manageService.insertServiceHistory(dto);

        rttr.addAttribute("user_no", dto.getUser_no());
        rttr.addAttribute("pageNum", pageNum);
        rttr.addAttribute("amount", amount);
        rttr.addFlashAttribute("msg", "진료 내역이 등록되었습니다.");

        return "redirect:/user_detail";
    }

    @GetMapping("/Certificate")
    public String Certificate(@RequestParam int user_no,
                              @RequestParam int service_no,
                              @RequestParam int pet_no,
                              // 💡 추가: 주민등록번호 13자리를 String으로 받습니다.
                              @RequestParam String id_number,
                              HttpSession session, Model model,
                              RedirectAttributes rttr) {
        String Role = (String) session.getAttribute("role");

        // 1. 관리자 권한 확인 (기존 로직 유지)
        if (!"ADMIN".equals(Role)) {
            rttr.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage";
        }

        // 2. 서버에서 주민등록번호 일치 여부를 검증하는 로직이 여기에 들어가야 합니다.
        //    (현재는 단순 전달만 구현)

        // 3. 확인서 데이터 로드 (기존 로직 유지)
        Map<String, Object> params = new HashMap<>();

        params.put("user_no", user_no);
        params.put("service_no", service_no);
        params.put("pet_no", pet_no);

        List<CertificateDTO> certificate = manageService.getCertificate(params);
        CertificateDTO cert = certificate.get(0);
        model.addAttribute("certificate", cert);

        // 💡 추가: 주민등록번호를 모델에 담아 JSP로 전달합니다.
        //    이 값은 Certificate.jsp에서 ${residentId}로 사용됩니다.
        model.addAttribute("residentId", id_number);

        return "Certificate"; // Certificate.jsp로 포워딩
    }

    @PostMapping("/user_serviceComplete")
    public String serviceComplete(
            @RequestParam("service_no") int service_no,
            @RequestParam("user_no") int user_no
    ) {
        manageService.completeService(service_no);
        return "redirect:/user_detail?user_no=" + user_no;
    }


    /**
     * 진료 예약 목록 관리 페이지를 처리합니다.
     * 관리자 권한을 확인하고, 페이징된 진료 예약 목록을 모델에 담아 전달합니다.
     *
     * @param cri 페이징/검색 조건을 담는 Criteria 객체
     */
    @GetMapping("/veterinaryRes_manage")
    public String VeterinaryResManagePage(@RequestParam(value = "status", required = false) String status,
                                          Criteria cri, HttpSession session,
                                          Model model,
                                          RedirectAttributes rttr) {
        String Role = (String) session.getAttribute("role");

        // 관리자 권한 확인
        if (!"ADMIN".equals(Role)) {
            rttr.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage";
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", status); // 상태 필터
        params.put("keyword", cri.getKeyword());

        // 전체 진료 예약 수 조회 및 PageDTO 생성
        int total = manageService.getVetResTotal(params, cri);
        model.addAttribute("pageMaker", new PageDTO(total, cri));

        // 페이징된 진료 예약 목록 조회
        List<MedicalResDTO> VeterinaryResList = manageService.VeterinaryResList(params, cri);
        model.addAttribute("VeterinaryResList", VeterinaryResList);

        return "veterinaryRes_manage";
    }

    /**
     * 미용 예약 목록 관리 페이지를 처리합니다.
     * 관리자 권한을 확인하고, 페이징된 미용 예약 목록을 모델에 담아 전달합니다.
     *
     * @param cri 페이징/검색 조건을 담는 Criteria 객체
     */
    @GetMapping("/groomingRes_manage")
    public String GroomingResManagePage(@RequestParam(value = "status", required = false) String status,
                                        Criteria cri, HttpSession session,
                                        Model model,
                                        RedirectAttributes rttr) {
        String Role = (String) session.getAttribute("role");

        // 관리자 권한 확인
        if (!"ADMIN".equals(Role)) {
            rttr.addFlashAttribute("alertMsg", "관리자만 접근 가능합니다.");
            return "redirect:/mainpage";
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", status); // 상태 필터
        params.put("keyword", cri.getKeyword());

        // 전체 미용 예약 수 조회 및 PageDTO 생성
        int total = manageService.getGroResTotal(params, cri);
        model.addAttribute("pageMaker", new PageDTO(total, cri));

        // 페이징된 미용 예약 목록 조회
        List<GroomingResDTO> GroomingResList = manageService.GroomingResList(params, cri);
        model.addAttribute("GroomingResList", GroomingResList);

        return "groomingRes_manage";
    }

    /**
     * 진료 또는 미용 예약을 '확정' 처리합니다.
     * 처리 후, 리다이렉트를 통해 기존 페이징 상태를 유지하며 목록으로 돌아갑니다.
     *
     * @param resNo   예약 번호
     * @param type    예약 타입 ("veterinary" 또는 "grooming")
     * @param pageNum 복귀할 페이지 번호
     * @param amount  페이지당 항목 수
     */
    @PostMapping("/confirmRes")
    public String confirmRes(
            @RequestParam("res_no") int resNo,
            @RequestParam("type") String type,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "amount", defaultValue = "10") int amount,
            RedirectAttributes rttr) {

        String tableName;
        String redirectPath;

        // 예약 타입에 따라 테이블 이름과 리다이렉트 경로 설정
        if ("veterinary".equals(type)) {
            tableName = "veterinary_res";
            redirectPath = "redirect:/veterinaryRes_manage";
        } else {
            tableName = "grooming_res";
            redirectPath = "redirect:/groomingRes_manage";
        }

        // 서비스 계층으로 전달할 파라미터 맵 생성
        Map<String, Object> params = new HashMap<>();
        params.put("res_no", resNo);
        params.put("tableName", tableName);

        manageService.confirmRes(params);

        // 리다이렉트 시 쿼리 파라미터로 pageNum과 amount를 추가하여 상태 유지
        rttr.addAttribute("pageNum", pageNum);
        rttr.addAttribute("amount", amount);
        rttr.addFlashAttribute("msg", "예약이 확정 처리되었습니다.");

        return redirectPath;
    }

    /**
     * 진료 또는 미용 예약을 '취소' 처리합니다.
     * 처리 후, 리다이렉트를 통해 기존 페이징 상태를 유지하며 목록으로 돌아갑니다.
     *
     * @param resNo        예약 번호
     * @param type         예약 타입 ("veterinary" 또는 "grooming")
     * @param cancelReason 취소 사유
     * @param pageNum      복귀할 페이지 번호
     * @param amount       페이지당 항목 수
     */
    @PostMapping("/cancelRes")
    public String cancelRes(
            @RequestParam("res_no") int resNo,
            @RequestParam("type") String type,
            @RequestParam("cancel_reason") String cancelReason,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "amount", defaultValue = "10") int amount,
            RedirectAttributes rttr) {

        String tableName;
        String redirectPath;

        // 예약 타입에 따라 테이블 이름과 리다이렉트 경로 설정
        if ("veterinary".equals(type)) {
            tableName = "veterinary_res";
            redirectPath = "redirect:/veterinaryRes_manage";
        } else {
            tableName = "grooming_res";
            redirectPath = "redirect:/groomingRes_manage";
        }

        // 서비스 계층으로 전달할 파라미터 맵 생성 (취소 사유 포함)
        Map<String, Object> params = new HashMap<>();
        params.put("res_no", resNo);
        params.put("tableName", tableName);
        params.put("cancel_reason", cancelReason);

        manageService.cancelRes(params);

        // 리다이렉트 시 쿼리 파라미터로 pageNum과 amount를 추가하여 상태 유지
        rttr.addAttribute("pageNum", pageNum);
        rttr.addAttribute("amount", amount);
        rttr.addFlashAttribute("msg", "예약이 취소 처리되었습니다.");
        //
        return redirectPath;
    }

    @PostMapping("/UserStatusProcess")
    public String UserStatusProcess(@RequestParam("user_no") int userNo,
                                    @RequestParam("targetStatus") String newStatus,
                                    @RequestParam(value = "suspension_reason", required = false) String suspensionReason,
                                    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "amount", defaultValue = "10") int amount,
                                    RedirectAttributes rttr) {

        Map<String, Object> params = new HashMap<>();
        params.put("user_no", userNo);
        params.put("newStatus", newStatus);

        String msg;

        if ("ACTIVE".equals(newStatus)) {
            // 활동 해제 요청: 사유를 NULL로 설정하여 DB에서 초기화
            params.put("suspension_reason", null);
            msg = "활동정지가 해제되었습니다.";
        } else {
            // 활동 정지 요청: 전송된 사유를 사용
            params.put("suspension_reason", suspensionReason);
            msg = "활동정지 처리되었습니다. 사유가 기록되었습니다.";
        }

        manageService.UserStatusProcess(params);

        rttr.addAttribute("user_no", userNo);
        rttr.addAttribute("pageNum", pageNum);
        rttr.addAttribute("amount", amount);
        rttr.addFlashAttribute("msg", msg);

        return "redirect:/user_detail";
    }
}