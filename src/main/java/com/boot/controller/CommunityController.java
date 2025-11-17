package com.boot.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;
import com.boot.dto.PageDTO;
import com.boot.service.CommunityService;


@Controller
public class CommunityController {
	
	@Autowired
	private CommunityService service;
	
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
	
}
