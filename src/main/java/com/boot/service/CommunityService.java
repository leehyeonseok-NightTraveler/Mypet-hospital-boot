package com.boot.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;

public interface CommunityService {

	public ArrayList<Mypet_CommunityDTO> getCommunityList(Criteria cri);
	public int getTotalCount();
	
//	글 상세조회
	public Mypet_CommunityDTO communityContentView(HashMap<String, String> param);
}
