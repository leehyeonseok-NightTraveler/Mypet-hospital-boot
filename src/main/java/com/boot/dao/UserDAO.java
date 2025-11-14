package com.boot.dao;

import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;

import com.boot.dto.Mypet_AdminDTO;
import com.boot.dto.Mypet_UserDTO;

@Mapper
public interface UserDAO {

    void join(Mypet_UserDTO dto);

    Integer loginFindNo(HashMap<String, Object> map);

    Mypet_UserDTO selectUserByNo(int no);
    Mypet_AdminDTO selectAdminByNo(int no);

    void updateUserInfo(HashMap<String, Object> map);

    int checkUserExists(HashMap<String, String> map);
    int updatePassword(HashMap<String, String> map);

    int checkDuplicateUserImage(String img_temp);
    void updateUserImage(HashMap<String, Object> map);
}
