	package com.boot.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.dao.UserDAO;
import com.boot.dto.Mypet_Community_CommentDTO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.service.CommunityCommentService;

@Controller
@RequestMapping("/comment")
public class CommentController {
	
	@Autowired
	private CommunityCommentService commentService;
	
	@Autowired
	private UserDAO dao;
	
	@RequestMapping("/save")
	@ResponseBody
	public ArrayList<Mypet_Community_CommentDTO> save(
	        @RequestParam HashMap<String, String> param,
	        HttpSession session) {

	    Mypet_UserDTO loginUser = (Mypet_UserDTO) session.getAttribute("loginUser");

	    if (loginUser == null) {
	        throw new RuntimeException("로그인이 필요합니다.");
	    }

	    // 로그인 정보 입력
	    param.put("user_no", String.valueOf(loginUser.getUser_no()));
	    param.put("user_name", loginUser.getUser_name());

	    // ★★★★★ 핵심 포인트 ★★★★★
	    String postNo = param.get("post_no");
	    param.put("post_no", postNo);

	    // 저장
	    commentService.save(param);

	    // 조회
	    return commentService.findAll(param);
	}



	
	@PostMapping("/deleteComment")
	@ResponseBody
	public String deleteComment(@RequestParam("comment_no") int commentNo) {

	    int result = commentService.deleteComment(commentNo);	

	    return (result == 1) ? "success" : "fail";
	}
	

}
