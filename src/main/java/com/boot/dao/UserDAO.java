package com.boot.dao;

import java.util.ArrayList; 
import java.util.HashMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.boot.dto.FindAccountDTO;
import com.boot.dto.Mypet_AdminDTO;
import com.boot.dto.Mypet_UserDTO;

@Mapper
public interface UserDAO {

    void join(Mypet_UserDTO dto);

    Integer loginFindNo(HashMap<String, Object> map);

    Mypet_UserDTO selectUserByNo(int no);
    Mypet_AdminDTO selectAdminByNo(int no);

    void updateUserInfo(HashMap<String, Object> map);

    int checkDuplicateUserImage(String img_temp);
    void updateUserImage(HashMap<String, Object> map);
    
//	아이디 찾기
	public ArrayList<FindAccountDTO> findAccount(HashMap<String, String>param);

//	비밀번호 찾기
	public ArrayList<FindAccountDTO> findPW(HashMap<String, String>param);
	
//	비밀번호 재설정
	void updateUserPwd(@Param("account_id") String id,
            @Param("account_pwd") String pw);

	void updateAdminPwd(@Param("account_id") String id,
             @Param("account_pwd") String pw);
}
