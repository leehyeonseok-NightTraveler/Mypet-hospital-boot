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

import com.boot.dto.Mypet_Qna_BoardDTO;
import com.boot.dto.Mypet_Qna_ReplyDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.service.QnaService;
import com.boot.service.UploadService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QnaController {

    private final QnaService service;
    
    private final UploadService uploadService;


    /* ============================
     *      Q&A 목록 페이지
     * ============================ */
    @GetMapping("/qna_page")
    public String qnaPage(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int currentPage
    ) {

        int pageSize = 10;
        int start = (currentPage - 1) * pageSize + 1;
        int end = currentPage * pageSize;

        HashMap<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);

        List<Mypet_Qna_BoardDTO> qnaList = service.list2(map);

        model.addAttribute("dd", qnaList);
        log.info("[Controller] Q&A 목록 불러오기 완료 ({}건)", qnaList.size());

        int totalCount = service.getTotalCount2();
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage);

        return "qna_page";
    }


    /* ============================
     *        Q&A 작성 페이지
     * ============================ */
    @GetMapping("/qna_write")
    public String qnaWrite(HttpSession session) {

        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) return "redirect:/login";

        return "qna_write";
    }


    /* ============================
     *        Q&A 등록 처리
     * ============================ */
    @PostMapping("/qna_write_ok")
    public String qnaWriteOk(
            Mypet_Qna_BoardDTO dto,
            @RequestParam(value = "qna_file_upload", required = false) MultipartFile file,
            HttpSession session,
            RedirectAttributes ra
    ) {

        Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) return "redirect:/login";

        dto.setUser_no(loginUser.getUser_no());
        log.info("[Controller] Q&A 등록 요청 수신: {}", dto);
        
        // 파일 저장 로직 추가
        if (file != null && !file.isEmpty()) {
        	String saved = uploadService.saveRawFile(file, "qna");
            dto.setQna_file(saved);   // DB 컬럼과 연결!
        }

        service.writeQna(dto);

        ra.addFlashAttribute("message", "문의가 등록되었습니다!");
        return "redirect:/qna_page";
    }
    
//    @GetMapping("/qna_view")
//    public String qnaView(
//            @RequestParam("qna_no") int qna_no,
//            Model model) {
//
//        Mypet_Qna_BoardDTO dto = service.getQna(qna_no); // 질문
//        Mypet_Qna_ReplyDTO reply = service.getReply(qna_no); // 답변
//
//        model.addAttribute("dto", dto);
//        model.addAttribute("reply", reply);
//
//        return "qna_content_view";
//    }
}
