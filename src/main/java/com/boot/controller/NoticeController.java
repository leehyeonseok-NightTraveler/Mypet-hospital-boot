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


    /* ============================
     *   공지사항 작성 페이지 이동
     * ============================ */
    @GetMapping("/notices_write_view")
    public String noticesWriteView(HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("loginAdmin") == null) {
            ra.addFlashAttribute("message", "관리자만 작성할 수 있습니다.");
            return "redirect:/notices_list";
        }
        return "notices_write_view";
    }


    /* ============================
     *       공지사항 등록
     * ============================ */
    @PostMapping("/notices_write")
    public String noticesWrite(
            Mypet_NoticesDTO dto,
            @RequestParam(value = "notice_file_upload", required = false) MultipartFile file,
            HttpSession session,
            RedirectAttributes ra
    ) {
        Mypet_AdminDTO admin = (Mypet_AdminDTO) session.getAttribute("loginAdmin");

        if (admin == null) {
            ra.addFlashAttribute("message", "관리자만 작성할 수 있습니다.");
            return "redirect:/notices_list";
        }

        dto.setAdmin_no(admin.getAdmin_no());

        // 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
        	String saved = uploadService.saveRawFile(file, "notices");
            dto.setNotice_file(saved);
        }

        service.writeNotice(dto);
        log.info("공지사항 등록 완료: {}", dto.getNotice_title());

        return "redirect:/notices_list";
    }


    /* ============================
     *     공지사항 수정 페이지 이동
     * ============================ */
    @GetMapping("/notices_modify_view")
    public String noticesModifyView(
            @RequestParam("notice_no") int notice_no,
            HttpSession session,
            Model model,
            RedirectAttributes ra
    ) {
        if (session.getAttribute("loginAdmin") == null) {
            ra.addFlashAttribute("message", "관리자만 수정할 수 있습니다.");
            return "redirect:/notices_list";
        }

        model.addAttribute("dto", service.getNoticeDetail(notice_no));
        return "notices_modify";
    }


    /* ============================
     *       공지사항 수정
     * ============================ */
    @PostMapping("/notices_modify")
    public String noticesModify(
            Mypet_NoticesDTO dto,
            @RequestParam(value = "notice_file_upload", required = false) MultipartFile file,
            HttpSession session,
            RedirectAttributes ra
    ) {
        if (session.getAttribute("loginAdmin") == null) {
            ra.addFlashAttribute("message", "관리자만 수정할 수 있습니다.");
            return "redirect:/notices_list";
        }

        // 기존 파일 조회
        Mypet_NoticesDTO old = service.getNoticeDetail(dto.getNotice_no());
        String oldFile = old.getNotice_file();

        // 새 파일 업로드
        if (file != null && !file.isEmpty()) {
        	  String saved = uploadService.saveRawFile(file, "notices");
            dto.setNotice_file(saved);

            // 기존 파일 삭제
            if (oldFile != null && !oldFile.isEmpty()) {
                uploadService.deleteFile(oldFile);
            }
        } else {
            // 파일 변경 없음 → 기존 파일 유지
            dto.setNotice_file(oldFile);
        }

        service.modifyNotice(dto);
        log.info("공지사항 수정 완료: {}", dto.getNotice_title());

        return "redirect:/notices_list";
    }



    /* ============================
     *       공지사항 삭제
     * ============================ */
    @PostMapping("/notices_delete")
    public String noticesDelete(
            @RequestParam("notice_no") int notice_no,
            HttpSession session,
            RedirectAttributes ra
    ) {
        if (session.getAttribute("loginAdmin") == null) {
            ra.addFlashAttribute("message", "관리자만 삭제할 수 있습니다.");
            return "redirect:/notices_list";
        }

        service.deleteNotice(notice_no);
        log.info("공지사항 삭제 완료: notice_no={}", notice_no);

        return "redirect:/notices_list";
    }


    /* ============================
     *       공지사항 목록
     * ============================ */
    @GetMapping("/notices_list")
    public String noticesList(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int currentPage
    ) {

        int pageSize = 10;
        int start = (currentPage - 1) * pageSize + 1;
        int end = currentPage * pageSize;

        HashMap<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);

        List<Mypet_NoticesDTO> notices = service.list(map);
        int totalCount = service.getTotalCount();
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("notices", notices);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage);

        return "notices_list";
    }


    /* ============================
     *     공지사항 상세 보기
     * ============================ */
    @GetMapping("/notices_view")
    public String noticesView(
            @RequestParam("notice_no") int notice_no,
            Model model
    ) {
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
