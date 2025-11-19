package com.boot.dao;

import java.util.ArrayList; 
import java.util.HashMap;

import com.boot.dto.Mypet_Community_CommentDTO;

public interface CommunityCommentDAO {
	public void save(HashMap<String, String> param);
	public ArrayList<Mypet_Community_CommentDTO> findAll(HashMap<String, String> param);
	
	public int deleteComment(int comment_no);
	public int deleteByPostNo(int postNo);
}
