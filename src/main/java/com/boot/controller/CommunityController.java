package com.boot.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boot.dao.UserDAO;
import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;
import com.boot.dto.Mypet_Community_CommentDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.dto.PageDTO;
import com.boot.service.CommunityCommentService;
import com.boot.service.CommunityCommentServiceImpl;
import com.boot.service.CommunityService;
import com.boot.service.UploadService;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class CommunityController {

    private final CommunityCommentServiceImpl communityCommentServiceImpl;
	
	@Autowired
	private CommunityService service;
	
	@Autowired
	private CommunityCommentService commentService;

	@Autowired
	private UserDAO dao;
	
	@Autowired
	private UploadService uploadService;

    CommunityController(CommunityCommentServiceImpl communityCommentServiceImpl) {
        this.communityCommentServiceImpl = communityCommentServiceImpl;
    }
	
	
	
    /* ============================
     *       자유게시판 목록
     * ============================ */

	@RequestMapping("/community_list")
	public String community_list(Criteria cri, Model model) {
		
		ArrayList<Mypet_CommunityDTO> list = service.getCommunityList(cri);
		int total = service.getTotalCount();
		
		model.addAttribute("list", list);
		model.addAttribute("pageMaker", new PageDTO(total, cri));
		
		return "community_list";
	}
	
 /* ============================
  *       글 보기
  * ============================ */
	
	
	@RequestMapping("/community_content_view")
	public String community_content_view(@RequestParam("postNo") int postNo,
	                                     @RequestParam HashMap<String, String> param,
	                                     Model model,
	                                     HttpSession session) {

	    // 로그인 유저 가져오기
	    Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");

	    if (loginUser != null) {
	        model.addAttribute("user_name", loginUser.getUser_name());
	        model.addAttribute("loginUserNo", loginUser.getUser_no());
	        model.addAttribute("sessionRole", session.getAttribute("role"));  //  추가
	    } else {
	        model.addAttribute("user_name", "비회원");
	        model.addAttribute("sessionRole", null);  // 추가
	    }

	    // 조회수 증가
	    service.increaseViewCount(postNo);

	    // 댓글 파라미터
	    param.put("post_no", String.valueOf(postNo));

	    // 댓글 목록
	    ArrayList<Mypet_Community_CommentDTO> commentList = commentService.findAll(param);
	    model.addAttribute("commentList", commentList);

	    // 본문
	    Mypet_CommunityDTO dto = service.communityContentView(param);
	    model.addAttribute("content_view", dto);

	    // 페이지 정보
	    model.addAttribute("pageMaker", param);

	    return "community_content_view";
	}

	
	/* ============================
     *       글 쓰기
     * ============================ */

    @RequestMapping("/community_write")
    public String community_write(
            @RequestParam HashMap<String, String> param,
            @RequestParam(value="post_file_upload", required=false) MultipartFile file,
            HttpSession session
    ) {

        Mypet_UserDTO user = (Mypet_UserDTO) session.getAttribute("loginUser");

        if (user == null) {
            return "redirect:login";
        }

        param.put("user_no", String.valueOf(user.getUser_no()));
        param.put("user_name", user.getUser_name());

        // 🔥 일반 첨부파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            // community 폴더에 원본명 유지로 저장됨
            String saved = uploadService.saveRawFile(file, "community");
            param.put("post_file", saved);   // DB 컬럼명에 맞춰 키 넣기!
        }

        // 게시글 저장
        service.communityWrite(param);

        return "redirect:community_list";
    }

	/* ============================
     *       글 삭제
     * ============================ */
	
	@RequestMapping("/community_delete")
	public String community_delete(@RequestParam HashMap<String, String> param, 
	                               HttpSession session, 
	                               Model model) {

	    // 로그인 체크
	    Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        model.addAttribute("msg", "로그인이 필요합니다.");
	        model.addAttribute("url", "/login");
	        return "alert";
	    }

	    String sessionUserNo = String.valueOf(loginUser.getUser_no());
	    String postNo = param.get("post_no");

	    // DB에서 글 정보 조회
	    HashMap<String, String> map = new HashMap<>();
	    map.put("postNo", postNo);

	    Mypet_CommunityDTO dto = service.communityContentView(map);
	    String writerNo = String.valueOf(dto.getUser_no());
	
	    // 관리자 권한 확인
	    String role = (String) session.getAttribute("role");

	    // 작성자 체크
	    if (!sessionUserNo.equals(writerNo) && !"ADMIN".equals(role)) {
	        model.addAttribute("msg", "삭제 권한이 없습니다.");
	        model.addAttribute("url",
	            "/community_content_view?postNo=" + postNo +
	            "&pageNum=" + param.get("pageNum") +
	            "&amount=" + param.get("amount")
	        );
	        return "alert";
	    }

	    /* ==============================
	     *   실제 파일 삭제 처리
	     * ============================== */

	    // 1) 첨부파일 삭제
	    if (dto.getPost_file() != null && !dto.getPost_file().isEmpty()) {
	        uploadService.deleteFile("community/" + dto.getPost_file());
	    }

	    // 2) summernote 본문에 포함된 이미지/비디오 삭제
	    uploadService.deleteSummernoteFiles("community", dto.getPost_content());

	    // 3) 댓글 삭제 (댓글 테이블이 있다면)
	    commentService.deleteByPostNo(Integer.parseInt(postNo));

	    // 4) 게시글 삭제
	    service.communityDelete(param);

	    return "redirect:community_list?pageNum=" + param.get("pageNum") 
	         + "&amount=" + param.get("amount");
	}

	
	/* ============================
     *       글 수정
     * ============================ */
	
	
	@RequestMapping("/community_modify")
	public String community_modify(@RequestParam HashMap<String, String> param,
	                               Model model,
	                               HttpSession session) {

	    // 로그인 유저 정보
	    Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        model.addAttribute("msg", "로그인이 필요합니다.");
	        model.addAttribute("url", "/login");
	        return "alert";
	    }

	    String sessionUserNo = String.valueOf(loginUser.getUser_no());

	    // 게시글 번호
	    String postNo = param.get("post_no");

	    // DB에서 실제 글 작성자 번호 조회
	    HashMap<String, String> map = new HashMap<>();
	    map.put("postNo", postNo);

	    Mypet_CommunityDTO dto = service.communityContentView(map);
	    String writerNo = String.valueOf(dto.getUser_no());

	    // 본인글 체크
	    if (!sessionUserNo.equals(writerNo)) {
	        model.addAttribute("msg", "본인 글만 수정할 수 있습니다.");
	        model.addAttribute("url", 
	            "/community_content_view?postNo=" + postNo +
	            "&pageNum=" + param.get("pageNum") +
	            "&amount=" + param.get("amount"));
	        return "alert";
	    }

	    // 통과 → 수정 페이지 이동
	    model.addAttribute("content_view", dto);
	    model.addAttribute("modify", param);

	    return "community_modify";
	}

	
	
	@RequestMapping("/community_modify_ok")
	public String community_modify(@RequestParam HashMap<String, String> param) {
		
		service.communityModify(param);
		
		return "redirect:community_list";
	}
	
	@GetMapping("/community_search")
	public String communitySearch(Criteria cri, Model model) {

	    // 검색된 전체 게시물 수
	    int total = service.searchCount(cri);

	    // 검색 + 페이징된 리스트
	    model.addAttribute("list", service.searchPostsPaging(cri));

	    // 페이징 처리
	    model.addAttribute("pageMaker", new PageDTO(total, cri));

	    // 검색파라미터 JSP 전달
	    model.addAttribute("param", cri);

	    return "community_list"; 
	}
	
}
