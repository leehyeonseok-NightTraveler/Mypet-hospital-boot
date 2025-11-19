package com.boot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.dao.CommunityDAO;
import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{
	
	
	@Autowired
	private SqlSession session;

	@Override
	public ArrayList<Mypet_CommunityDTO> getCommunityList(Criteria cri) {

		CommunityDAO dao = session.getMapper(CommunityDAO.class);
		ArrayList<Mypet_CommunityDTO> list = dao.getCommunityList(cri);
		
		return list;
	}

	@Override
	public int getTotalCount() {
		
		CommunityDAO dao = session.getMapper(CommunityDAO.class);
		int total = dao.getTotalCount();
		
		return total;
	}

//	글 상세목록 조회
	@Override
	public Mypet_CommunityDTO communityContentView(HashMap<String, String> param) {

		CommunityDAO dao = session.getMapper(CommunityDAO.class);
		Mypet_CommunityDTO dto = dao.communityContentView(param);
		
		return dto;
	}

//	글 쓰기
	@Override
	public void communityWrite(HashMap<String, String> param) {
		
		CommunityDAO dao = session.getMapper(CommunityDAO.class);
		dao.communityWrite(param);
		
	}

//	조회수 증가
	@Override
	public void increaseViewCount(int postNo) {
		CommunityDAO dao = session.getMapper(CommunityDAO.class);
	    dao.increaseViewCount(postNo);
	}

	@Override
	public void communityModify(HashMap<String, String> param) {

		CommunityDAO dao = session.getMapper(CommunityDAO.class);
		dao.communityModify(param);
		
	}

	@Override
	public void communityDelete(HashMap<String, String> param) {
		
//		게시글 삭제
		CommunityDAO dao = session.getMapper(CommunityDAO.class);
		dao.communityDelete(param);
	}
	
	
	@Autowired
	private CommunityDAO communityDAO;

	@Override
	public List<Mypet_CommunityDTO> searchPosts(Map<String, String> param) {
		
		return communityDAO.searchPosts(param);
	}

	@Override
	public List<Mypet_CommunityDTO> searchPostsPaging(Criteria cri) {
		
		return communityDAO.searchPostsPaging(cri);
	}

	@Override
	public int searchCount(Criteria cri) {
		
		return communityDAO.searchCount(cri);
	}

}
