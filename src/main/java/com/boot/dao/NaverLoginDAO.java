package com.boot.dao;

import org.apache.ibatis.annotations.Mapper;

import com.boot.dto.Mypet_UserDTO;

@Mapper
public interface NaverLoginDAO {
	// 1. 소셜 ID로 사용자 조회 (카카오와 통일)
    Mypet_UserDTO findUserBySocialId(String socialId);
    
    // 2. 신규 회원 가입 (카카오와 통일)
    void socialJoin_withDetails(Mypet_UserDTO userDTO); 
    
    // 3. 기존 회원 정보 갱신 (카카오와 통일)
    void socialUpdate_withDetails(Mypet_UserDTO userDTO);
}
