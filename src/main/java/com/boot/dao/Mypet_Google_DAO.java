package com.boot.dao;

import org.apache.ibatis.annotations.Mapper;
import com.boot.dto.Mypet_UserDTO;

@Mapper
public interface Mypet_Google_DAO {
    
    public Mypet_UserDTO findUserBySocialId(String socialId);
    
    public void socialJoin_withDetails(Mypet_UserDTO userDTO);
    
    public void socialUpdate_withDetails(Mypet_UserDTO userDTO);
}