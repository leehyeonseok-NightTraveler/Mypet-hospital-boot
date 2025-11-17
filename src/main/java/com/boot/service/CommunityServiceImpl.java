package com.boot.service;

import java.util.ArrayList;
import java.util.HashMap;

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
	

	
}
