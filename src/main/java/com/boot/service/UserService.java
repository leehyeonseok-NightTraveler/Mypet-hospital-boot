package com.boot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.boot.dto.Mypet_UserDTO;
import com.boot.dto.ServiceHistoryDTO;
import com.boot.dto.FindAccountDTO;
import com.boot.dto.GradeHistoryDTO;
import com.boot.dto.Mypet_PetDTO;

public interface UserService {

    void join(Mypet_UserDTO dto);

    Object login(HashMap<String, Object> map);

    void updateUserInfo(HashMap<String, Object> map);

    Mypet_UserDTO getUserByNo(int user_no);

    List<Mypet_PetDTO> getPetsByUserNo(int user_no);
    
    void updateUserImg(int user_no, String imgPath, String imgHash);
    boolean replaceUserImage(int userNo, MultipartFile file);
    
//	아이디 찾기
	public ArrayList<FindAccountDTO> findAccount(HashMap<String, String>param);

//	비밀번호 찾기
	public ArrayList<FindAccountDTO> findPW(HashMap<String, String>param);
	
//	비밀번호 재설정
	void updateUserPwd(String id, String pw);

	void updateAdminPwd(String id, String pw);
            
	// 멤버십 등급 히스토리 조회
	List<GradeHistoryDTO> getGradeHistory(int user_no);

	 // 이용 서비스 이력 조회
	List<ServiceHistoryDTO> getServiceHistory(int user_no);

}
