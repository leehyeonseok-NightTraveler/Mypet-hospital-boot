package com.boot.service;

import com.boot.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ManageService {
    List<Mypet_UserDTO> UserList(Criteria cri);
    Mypet_UserDTO UserInfo(int user_no);
    List<Mypet_PetDTO> PetList(int user_no);
    List<MedicalResDTO> VeterinaryResList(Criteria cri);
    List<GroomingResDTO> GroomingResList(Criteria cri);
    void confirmRes(Map<String, Object> params);
    void cancelRes(Map<String, Object> params);
    int getUserTotal(Criteria cri);
    int getVetResTotal(Criteria cri);
    int getGroResTotal(Criteria cri);
}
