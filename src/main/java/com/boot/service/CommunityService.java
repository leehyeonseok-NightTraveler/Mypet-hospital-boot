package com.boot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;

public interface CommunityService {

	public ArrayList<Mypet_CommunityDTO> getCommunityList(Criteria cri);
	public int getTotalCount();
	
//	글 상세조회
	public Mypet_CommunityDTO communityContentView(HashMap<String, String> param);
	
//	글 쓰기
	public void communityWrite(HashMap<String, String> param);
	
//	조회수 증가
	void increaseViewCount(int postNo);
	
	public void communityModify(HashMap<String, String> param);
	public void communityDelete(HashMap<String, String> param);
	
	List<Mypet_CommunityDTO> searchPosts(Map<String, String> param);
	
	// 페이징 검색 추가 (Criteria 기반)
	List<Mypet_CommunityDTO> searchPostsPaging(Criteria cri);

	// 검색 총 개수
	int searchCount(Criteria cri);
}
