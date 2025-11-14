package com.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import com.boot.dto.Mypet_UserDTO;

@Mapper
public interface Mypet_Kakao_DAO {
    
    public Mypet_UserDTO findUserBySocialId(String socialId);
    
    // (INSERT용)
    public void socialJoin_withDetails(Mypet_UserDTO userDTO);
    
    // 🔻🔻 (UPDATE용) 메소드 추가 🔻🔻
    public void socialUpdate_withDetails(Mypet_UserDTO userDTO);
}