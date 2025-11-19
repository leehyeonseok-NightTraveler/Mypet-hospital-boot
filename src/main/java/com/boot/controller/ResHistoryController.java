package com.boot.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // ⭐️ 중요!

import com.boot.dto.CalendarDTO;
import com.boot.dto.GroomingResDTO; // ⭐️ 미용 DTO 임포트 필요
import com.boot.dto.MedicalResDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.service.GroomingService; // ⭐️ 미용 서비스 임포트 필요
import com.boot.service.MedicalService;

import lombok.RequiredArgsConstructor;

@RestController // ⭐️ 1. JSON 데이터 반환 컨트롤러로 변경 (필수)
@RequestMapping("/api/history") // ⭐️ 2. 공통 주소 설정 (JSP의 fetch URL과 일치해야 함)
@RequiredArgsConstructor
public class ResHistoryController {
    
    private final MedicalService medicalService;
    private final GroomingService groomingService; // ⭐️ 3. 미용 서비스 주입

    // ===============================
    // 1. 진료 기록 API (/api/history/medical)
    // ===============================
    @GetMapping("/medical")
    public List<CalendarDTO> getMedicalEvents(HttpSession session) {
        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return null;

        HashMap<String, String> map = new HashMap<>();
        map.put("user_no", String.valueOf(loginUser.getUser_no()));

        // 진료 기록 가져오기
        List<MedicalResDTO> medicalList = medicalService.findMedicalReservations(map);
        
        List<CalendarDTO> events = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        for (MedicalResDTO med : medicalList) {
            CalendarDTO event = new CalendarDTO();
            event.setTitle("[" + med.getPet_name() + "] " + med.getService_item());
            
            if (med.getRes_date() != null) {
                event.setStart(sdf.format(med.getRes_date()));
            }
            
            event.setColor("#ff6b6b"); // 빨간색 (진료)
            events.add(event);
        }

        return events;
    }

    // ===============================
    // 2. 미용 기록 API (/api/history/grooming)
    // ===============================
    @GetMapping("/grooming")
    public List<CalendarDTO> getGroomingEvents(HttpSession session) {
        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return null;

        HashMap<String, String> map = new HashMap<>();
        map.put("user_no", String.valueOf(loginUser.getUser_no()));

        // ⭐️ 미용 기록 가져오기 (GroomingService에 해당 메소드가 있어야 함)
        // (findGroomingReservations 메소드 이름은 실제 서비스 코드에 맞게 수정하세요)
        List<GroomingResDTO> groomingList = groomingService.findGroomingReservations(map);
        
        List<CalendarDTO> events = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        for (GroomingResDTO groom : groomingList) {
            CalendarDTO event = new CalendarDTO();
            // 미용 DTO에 맞는 필드 사용 (예: getStyle_name 등)
            event.setTitle("[" + groom.getPet_name() + "] " + groom.getService_item());
            
            if (groom.getRes_date() != null) {
                event.setStart(sdf.format(groom.getRes_date()));
            }
            
            event.setColor("#4d96f7"); // 파란색 (미용)
            events.add(event);
        }

        return events;
    }
}