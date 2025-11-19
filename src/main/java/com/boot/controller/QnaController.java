package com.boot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.boot.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.service.QnaService;
import com.boot.service.UploadService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QnaController {

    private final QnaService service;
    private final UploadService uploadService;


    @GetMapping("/qna_page")
    public String qnaPage( Model model, Criteria cri) {

        List<Mypet_Qna_BoardDTO> qnaList = service.getQnaList(cri);
        model.addAttribute("qnaList", qnaList);

        int total = service.getQnaTotal();
        model.addAttribute("pageMaker", new PageDTO(total, cri));

        return "qna_page";
    }

    @GetMapping("/qna_write")
    public String qnaWriteView() {
        return "qna_write";  // JSP 이름
    }


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

        // 💡 파일 처리: 파일이 존재하면 저장 후 DTO에 파일명 설정
        if (file != null && !file.isEmpty()) {
            String saved = uploadService.saveRawFile(file, "qna");
            dto.setQna_file(saved);
        }

        service.writeQna(dto);

        ra.addFlashAttribute("message", "문의가 등록되었습니다!");
        return "redirect:/qna_page";
    }

    @GetMapping("/qna_view")
    public String qnaView(
            @RequestParam int qna_no,
            Model model, HttpSession session,
            Criteria cri) {

        String role = null;
        Integer userNo = null;

        // 💡 사용자 또는 관리자 로그인 정보 확인 및 역할(Role) 설정
        Object userObj = session.getAttribute("loginUser");
        if (userObj != null && userObj instanceof Mypet_UserDTO) {
            Mypet_UserDTO loginUser = (Mypet_UserDTO) userObj;
            userNo = loginUser.getUser_no();
        }

        Object adminObj = session.getAttribute("loginAdmin");
        if (adminObj != null && adminObj instanceof Mypet_AdminDTO) {
            role = "ADMIN"; // 관리자일 경우 role 설정
        }

        // 💡 JSP에서 삭제/수정 권한 판단을 위해 모델에 role 및 userNo 전달
        model.addAttribute("role", role);
        model.addAttribute("user_no", userNo);

        model.addAttribute("cri", cri);

        // 질문 및 답변 조회
        Mypet_Qna_BoardDTO detail = service.getQnaDetail(qna_no);
        Mypet_Qna_ReplyDTO reply = service.getQnaReply(qna_no);

        model.addAttribute("detail", detail);
        model.addAttribute("reply", reply);

        return "qna_content_view";
    }

    @PostMapping("/ReplyProcess")
    public String ReplyProcess(@RequestParam String mode,
                               @RequestParam String reply_content,
                               @RequestParam int qna_no,
                               HttpSession session) {
        Object loginObj = session.getAttribute("loginAdmin");

        // 💡 관리자 객체를 가져옴. (Null 체크는 호출하는 JSP에서 제어한다고 가정)
        Mypet_AdminDTO loginAdmin = (Mypet_AdminDTO) loginObj;
        int adminNo = loginAdmin.getAdmin_no();

        String redirectPath;
        Map<String, Object> params = new HashMap<>();

        if ("create".equals(mode)) {
            // 💡 답변 등록 로직: 답변 상태 업데이트 및 답변 저장
            redirectPath = "redirect:/qna_view?qna_no=" + qna_no;

            params.put("admin_no", adminNo);
            params.put("qna_no", qna_no);
            params.put("reply_content", reply_content);

            service.qnaStatusUpdate(qna_no); // 상태를 '답변 완료' 등으로 변경
            service.writeReply(params);
        } else { // mode is "modify"
            // 💡 답변 수정 로직
            redirectPath = "redirect:/qna_view?qna_no=" + qna_no;

            params.put("qna_no", qna_no);
            params.put("reply_content", reply_content);

            service.modifyReply(params);
        }
        return redirectPath;
    }

    @PostMapping("/qna_delete")
    public String deleteQna(@RequestParam int qna_no) {
        // 💡 Q&A 삭제 로직: 답변 먼저 삭제 후 질문 삭제
        service.deleteReplyByQnaNo(qna_no);
        service.deleteQna(qna_no);
        return "redirect:/qna_page";
    }

    @GetMapping("/qna_modify")
    public String qnaModifyForm(
            @RequestParam int qna_no,
            @RequestParam(defaultValue="1") int pageNum,
            @RequestParam(defaultValue="10") int amount,
            Model model
    ) {
        Mypet_Qna_BoardDTO detail = service.getQnaDetail(qna_no);

        model.addAttribute("detail", detail);
        model.addAttribute("cri", new Criteria(pageNum, amount));

        return "qna_modify";
    }


    
    @PostMapping("/qna_modify")
    public String qnaModify(
            Mypet_Qna_BoardDTO dto,
            @RequestParam(value="qna_file_upload", required=false) MultipartFile file
    ) {

        // 기존 데이터 조회
        Mypet_Qna_BoardDTO old = service.getQnaDetail(dto.getQna_no());
        String oldFile = old.getQna_file();

        // 새 파일 업로드된 경우
        if (file != null && !file.isEmpty()) {
            String saved = uploadService.saveRawFile(file, "qna");
            dto.setQna_file(saved);

            // 기존 파일 삭제
            if (oldFile != null && !oldFile.isEmpty()) {
                uploadService.deleteFile("qna/" + oldFile);
            }
        } else {
            // 변경 없으면 기존 파일 유지
            dto.setQna_file(oldFile);
        }

        service.modifyQna((Map<String, Object>) dto);

        return "redirect:/qna_content_view?qna_no=" + dto.getQna_no();
    }


    @PostMapping("/QnaModifyProcess")
    public String QnaModifyProcess(
            @RequestParam String qna_title,
            @RequestParam String qna_content,
            @RequestParam int qna_no,
            @RequestParam(required = false) MultipartFile qna_newFile,
            @RequestParam(required = false) String original_file,
            @RequestParam int pageNum,
            @RequestParam int amount,
            RedirectAttributes rttr
    ) {

        String finalFile = original_file; // 기본값 = 기존 파일

        // 새 파일 업로드 시
        if (qna_newFile != null && !qna_newFile.isEmpty()) {
            finalFile = uploadService.saveRawFile(qna_newFile, "qna");

            // 기존 파일 삭제
            if (original_file != null && !original_file.isEmpty()) {
                uploadService.deleteFile("qna/" + original_file);
            }
        }

        // 파라미터 전달
        Map<String, Object> params = new HashMap<>();
        params.put("qna_title", qna_title);
        params.put("qna_content", qna_content);
        params.put("qna_no", qna_no);
        params.put("qna_file", finalFile);   // ⭐⭐ 문제 해결 핵심

        service.modifyQna(params);

        rttr.addAttribute("pageNum", pageNum);
        rttr.addAttribute("amount", amount);

        return "redirect:/qna_view?qna_no=" + qna_no;
    }

}