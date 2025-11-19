package com.boot.service;

import java.util.ArrayList; 
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.dao.CommunityCommentDAO;
import com.boot.dto.Mypet_Community_CommentDTO;

@Service
public class CommunityCommentServiceImpl implements CommunityCommentService{

	
	  
	@Autowired
	private SqlSession sqlSession;

	@Override
	public void save(HashMap<String, String> param) {
		CommunityCommentDAO dao = sqlSession.getMapper(CommunityCommentDAO.class);
		dao.save(param);
	}

	@Override
	public ArrayList<Mypet_Community_CommentDTO> findAll(HashMap<String, String> param) {
		CommunityCommentDAO dao = sqlSession.getMapper(CommunityCommentDAO.class);
		ArrayList<Mypet_Community_CommentDTO> list = dao.findAll(param);
		return list;
	}
	
	@Override
	public int deleteComment(int comment_no) {
		CommunityCommentDAO dao = sqlSession.getMapper(CommunityCommentDAO.class);
	    return dao.deleteComment(comment_no);
	}
	
	@Override
	public int deleteByPostNo(int postNo) {
	    CommunityCommentDAO dao = sqlSession.getMapper(CommunityCommentDAO.class);
	    return dao.deleteByPostNo(postNo);
	}
}




