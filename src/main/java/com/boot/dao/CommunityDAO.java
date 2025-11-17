package com.boot.dao;

import java.util.ArrayList; 

import org.apache.ibatis.annotations.Mapper;

import com.boot.dto.Criteria;
import com.boot.dto.Mypet_CommunityDTO;

@Mapper
public interface CommunityDAO {
	
	public ArrayList<Mypet_CommunityDTO> getCommunityList(Criteria cri);
	public int getTotalCount();
}
