package com.boot.service;

import java.util.ArrayList; 
import java.util.HashMap;

import com.boot.dto.Mypet_Community_CommentDTO;

public interface CommunityCommentService {
	public void save(HashMap<String, String> param);
	public ArrayList<Mypet_Community_CommentDTO> findAll(HashMap<String, String> param);
	
	public int deleteComment(int comment_no);
	public int deleteByPostNo(int postNo);
}
