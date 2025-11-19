package com.boot.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.dto.PageDTO;
import com.boot.service.CommunityCommentService;
import com.boot.service.CommunityCommentServiceImpl;
import com.boot.service.CommunityService;


@Controller
public class CommunityController {

    private final CommunityCommentServiceImpl communityCommentServiceImpl;
	
	@Autowired
	private CommunityService service;
	
	@Autowired
	private CommunityCommentService commentService;


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
	                                     Model model) {

	    service.increaseViewCount(postNo);

	    // ⭐ 삭제 + 상세 조회 모두 post_no 로 사용
	    param.put("post_no", String.valueOf(postNo));

	    Mypet_CommunityDTO dto = service.communityContentView(param);
	    model.addAttribute("content_view", dto);

	    model.addAttribute("pageMaker", param);
	    
	    return "community_content_view";
	}

	
	/* ============================
     *       글 쓰기
     * ============================ */
	
	@RequestMapping("/community_write")
	public String community_write(@RequestParam HashMap<String, String> param, HttpSession session) {
		
			Mypet_UserDTO user = (Mypet_UserDTO) session.getAttribute("loginUser");
			
			if (user == null) {

		        return "redirect:login";
		    }
			
			
		    param.put("user_no", String.valueOf(user.getUser_no()));
		    param.put("user_name", user.getUser_name());
		
		service.communityWrite(param);
		
		return "redirect:community_list";
	}

	/* ============================
     *       글 삭제
     * ============================ */
	
	@RequestMapping("/community_delete")
	public String community_delete(@RequestParam HashMap<String, String> param, HttpSession session, Model model) {

	    Object u = session.getAttribute("user_no");
	    String sessionUserNo = (u == null) ? "" : String.valueOf(u);
	    
	    String writerNo = param.get("user_no");
		
	    System.out.println("### DELETE PARAM = " + param);

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

	    // 세션에서 로그인한 사용자 번호 가져오기
	    Object u = session.getAttribute("user_no");
	    String sessionUserNo = (u == null) ? "" : String.valueOf(u);

	    // 글 작성자 번호
	    String writerNo = param.get("user_no");

	    // 본인글 아닌 경우 → alert.jsp로 이동
	    if (sessionUserNo.isEmpty() || writerNo == null || !sessionUserNo.equals(writerNo)) {

	        model.addAttribute("msg", "본인 글만 수정할 수 있습니다.");
	        model.addAttribute("url",
	            "/community_content_view?postNo=" + param.get("post_no")
	            + "&pageNum=" + param.get("pageNum")
	            + "&amount=" + param.get("amount")
	        );

	        return "alert"; // alert.jsp
	    }

	    // 본인이 맞으면 수정 페이지로
	    model.addAttribute("modify", param);

	    return "community_modify";
	}

	
	
	@RequestMapping("/community_modify_ok")
	public String community_modify(@RequestParam HashMap<String, String> param) {
		
		service.communityModify(param);
		
		return "redirect:community_list";
	}
}
















