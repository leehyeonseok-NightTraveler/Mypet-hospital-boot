package com.boot.service;

import com.boot.dto.GroomingResDTO;
import com.boot.dto.MedicalResDTO;
import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.Mypet_UserDTO;

import java.util.List;
import java.util.Map;

public interface ManageService {
    List<Mypet_UserDTO> UserList();
    Mypet_UserDTO UserInfo(int user_no);
    List<Mypet_PetDTO> PetList(int user_no);
    List<MedicalResDTO> VeterinaryResList();
    List<GroomingResDTO> GroomingResList();
    void confirmRes(Map<String, Object> params);
    void cancelRes(Map<String, Object> params);
}
