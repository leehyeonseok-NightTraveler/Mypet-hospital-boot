package com.boot.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;

@Mapper
public interface CommunityDAO {
	
//	페이징 및 글 목록
	public ArrayList<Mypet_CommunityDTO> getCommunityList(Criteria cri);
	public int getTotalCount();
	
//	글 상세조회
	public Mypet_CommunityDTO communityContentView(HashMap<String, String> param);
	
//	글 쓰기
	public void communityWrite(HashMap<String, String> param);
	
}
