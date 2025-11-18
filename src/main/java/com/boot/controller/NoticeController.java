package com.boot.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.dto.Mypet_AdminDTO;
import com.boot.dto.Mypet_NoticesDTO;
import com.boot.service.NoticeService;
import com.boot.service.UploadService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService service;
    private final UploadService uploadService;

    // 관리자 권한 확인 (ROLE 세션만 확인)
    private boolean checkAdmin(HttpSession session, RedirectAttributes ra) {
        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            ra.addFlashAttribute("message", "관리자만 접근할 수 있습니다.");
            return false;
        }
        return true;
    }


    /* 공지사항 작성 페이지로 이동 */
    @GetMapping("/notices_write_view")
    public String noticesWriteView(HttpSession session, RedirectAttributes ra) {
        // 관리자 권한 확인
        if (!checkAdmin(session, ra)) {
            return "redirect:/notices_list";
        }
        return "notices_write_view";
    }


    /* 새로운 공지사항 등록 처리 */
    @PostMapping("/notices_write")
    public String noticesWrite(
            Mypet_NoticesDTO dto,
            @RequestParam(value = "notice_file_upload", required = false) MultipartFile file,
            HttpSession session,
            RedirectAttributes ra
    ) {
        // 관리자 권한 확인
        if (!checkAdmin(session, ra)) {
            return "redirect:/notices_list";
        }

        // 세션에서 관리자 DTO 객체 및 유효성 확인
        Mypet_AdminDTO admin = (Mypet_AdminDTO) session.getAttribute("loginAdmin");
        if (admin == null) {
            ra.addFlashAttribute("message", "관리자 세션 정보가 유효하지 않습니다. 다시 로그인해주세요.");
            return "redirect:/login";
        }

        // 작성자 번호 설정
        dto.setAdmin_no(admin.getAdmin_no());

        // 첨부 파일 저장 및 DTO에 파일 이름 설정
        if (file != null && !file.isEmpty()) {
            String saved = uploadService.saveRawFile(file, "notices");
            dto.setNotice_file(saved);
        }

        service.writeNotice(dto);
        log.info("공지사항 등록 완료: {}", dto.getNotice_title());

        return "redirect:/notices_list";
    }


    /* 공지사항 수정 페이지로 이동 */
    @GetMapping("/notices_modify_view")
    public String noticesModifyView(
            @RequestParam("notice_no") int notice_no,
            HttpSession session,
            Model model,
            RedirectAttributes ra
    ) {
        // 관리자 권한 확인
        if (!checkAdmin(session, ra)) {
            return "redirect:/notices_list";
        }

        // 상세 정보 조회 후 모델에 추가
        Mypet_NoticesDTO notice = service.getNoticeDetail(notice_no);
        model.addAttribute("dto", notice);

        return "notices_modify";
    }


    /* 공지사항 수정 처리 */
    @PostMapping("/notices_modify")
    public String noticesModify(
            Mypet_NoticesDTO dto,
            @RequestParam(value = "notice_file_upload", required = false) MultipartFile file,
            HttpSession session,
            RedirectAttributes ra
    ) {
        // 관리자 권한 확인
        if (!checkAdmin(session, ra)) {
            return "redirect:/notices_list";
        }

        // 기존 파일 정보 조회
        Mypet_NoticesDTO old = service.getNoticeDetail(dto.getNotice_no());
        String oldFile = old.getNotice_file();

        // 새 파일이 업로드된 경우
        if (file != null && !file.isEmpty()) {
            // 새 파일 저장 및 DTO 업데이트
            String saved = uploadService.saveRawFile(file, "notices");
            dto.setNotice_file(saved);

            // 기존 파일 삭제 처리
            if (oldFile != null && !oldFile.isEmpty()) {
                uploadService.deleteFile(oldFile);
            }
        } else {
            // 파일 변경 없으면 기존 파일 유지
            dto.setNotice_file(oldFile);
        }

        service.modifyNotice(dto);
        log.info("공지사항 수정 완료: {}", dto.getNotice_title());

        return "redirect:/notices_list";
    }


    /* 공지사항 삭제 처리 */
    @PostMapping("/notices_delete")
    public String noticesDelete(
            @RequestParam("notice_no") int notice_no,
            HttpSession session,
            RedirectAttributes ra
    ) {
        // 관리자 권한 확인
        if (!checkAdmin(session, ra)) {
            return "redirect:/notices_list";
        }

        // 파일 경로 조회 후 실제 파일 삭제 처리
        Mypet_NoticesDTO dto = service.getNoticeDetail(notice_no);
        if (dto != null && dto.getNotice_file() != null && !dto.getNotice_file().isEmpty()) {
            uploadService.deleteFile(dto.getNotice_file());
        }

        service.deleteNotice(notice_no);
        log.info("공지사항 삭제 완료: notice_no={}", notice_no);

        return "redirect:/notices_list";
    }


    /* 공지사항 목록 조회 및 페이징 */
    @GetMapping("/notices_list")
    public String noticesList(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int currentPage
    ) {
        int pageSize = 10;
        int start = (currentPage - 1) * pageSize + 1;
        int end = currentPage * pageSize;

        // DB 조회를 위한 페이지 범위 설정
        HashMap<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);

        List<Mypet_NoticesDTO> notices = service.list(map);
        int totalCount = service.getTotalCount();
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        // 모델에 데이터 추가
        model.addAttribute("notices", notices);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage);

        return "notices_list";
    }


    /* 공지사항 상세 보기 */
    @GetMapping("/notices_view")
    public String noticesView(
            @RequestParam("notice_no") int notice_no,
            Model model,
            HttpSession session // 세션 기반 조회수 중복 방지를 위해 추가
    ) {
        String noticeId = "notice_view_" + notice_no;

        // 세션당 1회만 조회수 증가 처리 (2회 증가 버그 방지)
        if (session.getAttribute(noticeId) == null) {
            service.increaseNoticeViewCount(notice_no);
            session.setAttribute(noticeId, true);
        }

        // 상세 정보 조회
        Mypet_NoticesDTO dto = service.getNoticeDetail(notice_no);

        if (dto == null) {
            model.addAttribute("message", "해당 공지사항을 찾을 수 없습니다.");
            return "errorPage";
        }

        model.addAttribute("dto", dto);
        log.info("공지사항 상세 조회 완료: {}", dto.getNotice_title());

        return "notices_content_view";
    }
}