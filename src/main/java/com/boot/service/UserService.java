package com.boot.service;

import java.util.HashMap;
import java.util.List;
import com.boot.dto.Mypet_UserDTO;
import com.boot.dto.Mypet_PetDTO;

public interface UserService {

    void join(Mypet_UserDTO dto);

    Object login(HashMap<String, Object> map);

    void updateUserInfo(HashMap<String, Object> map);

    Mypet_UserDTO getUserByNo(int user_no);

    List<Mypet_PetDTO> getPetsByUserNo(int user_no);

    boolean checkUserExists(HashMap<String, String> map);
    boolean updatePassword(HashMap<String, String> map);
    
    boolean uploadUserImage(int user_no, String fileName, byte[] fileBytes);
}
