package com.boot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.boot.dao.UserDAO;
import com.boot.dao.PetDAO;
import com.boot.dto.Mypet_UserDTO;
import com.boot.util.ImageHashUtil;
import com.boot.dto.Mypet_PetDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PetDAO petDAO;

    @Override
    public void join(Mypet_UserDTO dto) {
        userDAO.join(dto);
    }

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
    public boolean uploadUserImage(int user_no, String fileName, byte[] fileBytes) {
        try {
            String hash = ImageHashUtil.getReadableHash(fileBytes);

            if (userDAO.checkDuplicateUserImage(hash) > 0) {
                return false;  // 중복 이미지
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("user_no", user_no);
            map.put("user_img", "/resources/upload/user/" + fileName);
            map.put("user_img_temp", hash);

            userDAO.updateUserImage(map);
            return true;

        } catch (Exception e) {
            log.error("User image upload failed", e);
            return false;
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

    @Override
    public boolean checkUserExists(HashMap<String, String> map) {
        return userDAO.checkUserExists(map) == 1;
    }

    @Override
    public boolean updatePassword(HashMap<String, String> map) {
        return userDAO.updatePassword(map) == 1;
    }
}
