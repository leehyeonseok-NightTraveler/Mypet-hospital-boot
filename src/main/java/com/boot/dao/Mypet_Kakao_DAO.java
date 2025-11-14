package com.boot.dao; // 👈 본인의 DAO 패키지 경로

import com.boot.dto.Mypet_UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Mypet_Kakao_DAO {
    
    Mypet_UserDTO findUserBySocialId(String socialId);
    void socialJoin(Mypet_UserDTO dto);
}