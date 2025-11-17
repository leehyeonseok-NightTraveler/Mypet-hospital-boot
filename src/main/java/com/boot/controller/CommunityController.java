package com.boot.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;
import com.boot.dto.Mypet_Community_CommentDTO;
import com.boot.dto.PageDTO;
import com.boot.service.CommunityCommentService;
import com.boot.service.CommunityService;


@Controller
public class CommunityController {
	
	@Autowired
	private CommunityService service;
	
	@Autowired
	private CommunityCommentService commentService;
	
	
	
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
	
	@RequestMapping("/community_content_view")
	public String community_content_view(@RequestParam HashMap<String, String> param, Model model) {

		Mypet_CommunityDTO dto = service.communityContentView(param);
		model.addAttribute("content_view", dto);
		
		// 해당 게시글에 작성된 댓글 리스트를 가져옴
		ArrayList<Mypet_Community_CommentDTO> commentList = commentService.findAll(param);
		
		model.addAttribute("commentList", commentList);
		
//		content_voew.jsp에서 pageMaker를 가지고 페이징 처리
		model.addAttribute("pageMaker", param);
				
		return "community_content_view";
	}
	
	
}
