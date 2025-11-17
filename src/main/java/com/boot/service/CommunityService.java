package com.boot.service;

import java.util.ArrayList;

import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;

public interface CommunityService {

	public ArrayList<Mypet_CommunityDTO> getCommunityList(Criteria cri);
	public int getTotalCount();
}
