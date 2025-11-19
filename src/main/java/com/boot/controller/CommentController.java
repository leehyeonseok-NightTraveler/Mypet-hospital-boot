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
	public @ResponseBody ArrayList<Mypet_Community_CommentDTO> save(@RequestParam HashMap<String, String> param
			, Model model,HttpSession session) {
		
		Object u = session.getAttribute("user_no");
		if (u == null) {
	        throw new RuntimeException("로그인이 필요합니다.");
	    }
		int userNo = Integer.parseInt(u.toString());
		
		Mypet_UserDTO user = dao.selectUserByNo(userNo);
		
		param.put("user_no", String.valueOf(user.getUser_no()));
	    param.put("user_name", user.getUser_name());
		
		commentService.save(param);
		
		// 해당 게시글에 작성된 댓글 리스트를 가져옴
		ArrayList<Mypet_Community_CommentDTO> commentList = commentService.findAll(param);
		
		return commentList;
	}
	
	@PostMapping("/deleteComment")
	@ResponseBody
	public String deleteComment(@RequestParam("comment_no") int commentNo) {

	    int result = commentService.deleteComment(commentNo);	

	    return (result == 1) ? "success" : "fail";
	}
	

}
