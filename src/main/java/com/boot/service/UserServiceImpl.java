package com.boot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.boot.dao.UserDAO;
import com.boot.dao.GradeHistoryDAO;
import com.boot.dao.PetDAO;
import com.boot.dao.ServiceHistoryDAO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.dto.ServiceHistoryDTO;
import com.boot.dto.FindAccountDTO;
import com.boot.dto.GradeHistoryDTO;
import com.boot.dto.Mypet_PetDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PetDAO petDAO;
    private final UploadService uploadService;

    @Override
    public void join(Mypet_UserDTO dto) {
        userDAO.join(dto);
    }
    
    @Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private UserDAO dao;

    @Override
    public Object login(HashMap<String, Object> map) {

        Integer no = userDAO.loginFindNo(map);
        if (no == null) return null;

        // admin 처리
        if (no >= 5001) {
            return userDAO.selectAdminByNo(no);
        }

        // user 처리
        if (no >= 1) {
            Mypet_UserDTO user = userDAO.selectUserByNo(no);

            if (user != null) {
                HashMap<String, Object> petsMap = new HashMap<>();
                petsMap.put("user_no", user.getUser_no());
                user.setPets(petDAO.selectPetsByUserNo(petsMap));
            }

            return user;
        }

        return null;
    }
    
    @Override
    public void updateUserImg(int user_no, String imgPath, String imgHash) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_no", user_no);
        map.put("user_img", imgPath);
        map.put("user_img_temp", imgHash);

        userDAO.updateUserImage(map);
    }
    
    @Override
    public boolean replaceUserImage(int userNo, MultipartFile file) {

        // 1) 기존 유저 정보 조회
        Mypet_UserDTO user = userDAO.selectUserByNo(userNo);
        if (user == null) return false;

        String oldImg = user.getUser_img();  // 기존 이미지 경로 (/YYYY/MM/DD/UUID_HASH_name.jpg)

        try {
            // 2) 새 이미지 저장
            String saved = uploadService.saveImageWithHash(file, "user");

            // 3) 해시 추출
            String fileName = saved.substring(saved.lastIndexOf("/") + 1);
            String hash = fileName.split("_")[1];

            // 4) DB 업데이트
            HashMap<String, Object> map = new HashMap<>();
            map.put("user_no", userNo);
            map.put("user_img", saved);
            map.put("user_img_temp", hash);
            userDAO.updateUserImage(map);

            // 5) 기존 파일 삭제
            if (oldImg != null && !oldImg.isEmpty()) {
                uploadService.deleteFile(oldImg);
            }

            return true;

        } catch (Exception e) {
            log.error("유저 이미지 교체 오류", e);
            throw new RuntimeException("유저 이미지 변경 실패");
        }
    }



    @Override
    public void updateUserInfo(HashMap<String, Object> map) {
        userDAO.updateUserInfo(map);
    }

    @Override
    public Mypet_UserDTO getUserByNo(int user_no) {
        return userDAO.selectUserByNo(user_no);
    }

    @Override
    public List<Mypet_PetDTO> getPetsByUserNo(int user_no) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_no", user_no);
        return petDAO.selectPetsByUserNo(map);
    }
    
	//    아이디 찾기
	@Override
	public ArrayList<FindAccountDTO> findAccount(HashMap<String, String> param) {
	
	UserDAO dao = sqlSession.getMapper(UserDAO.class);
	ArrayList<FindAccountDTO>list = dao.findAccount(param);
	
	return list;
	}
	
	//비밀번호 찾기
	@Override
	public ArrayList<FindAccountDTO> findPW(HashMap<String, String> param) {
	
	UserDAO dao = sqlSession.getMapper(UserDAO.class);
	ArrayList<FindAccountDTO>list = dao.findPW(param);
	
	return list;
	}
	
	//비밀번호 재설정
	@Override
	public void updateUserPwd(String id, String pw) {
		dao.updateUserPwd(id, pw);
		
	}

	@Override
	public void updateAdminPwd(String id, String pw) {
		dao.updateAdminPwd(id, pw);
		
	}
	
	@Autowired
	private GradeHistoryDAO gradeHistoryDAO;

	@Autowired
	private ServiceHistoryDAO serviceHistoryDAO;

	@Override
	public List<GradeHistoryDTO> getGradeHistory(int user_no) {
	    return gradeHistoryDAO.getGradeHistory(user_no);
	}

	@Override
	public List<ServiceHistoryDTO> getServiceHistory(int user_no) {
	    return serviceHistoryDAO.getServiceHistory(user_no);
	}

	
}
