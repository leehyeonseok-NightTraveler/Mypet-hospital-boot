package com.boot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.PetWeightDTO;

public interface PetService {

    Mypet_PetDTO getPetInfo(int user_no, int pet_no);

    List<Mypet_PetDTO> getPetsByUserNo(int user_no);

    void petjoin(Mypet_PetDTO petDTO);

    void updatePetInfo(HashMap<String, Object> map);

    Mypet_PetDTO getPetByNo(int pet_no);
    
    void updatePetImg(int pet_no, String imgPath, String imgHash);
    void replacePetImage(int petNo, MultipartFile file);
    
    //1. 반려동물 체중 기록 추가
    public void addPetWeight(int petNo, double weight);
    //2. 특정 반려동물의 전체 체중 기록 조회 (그래프 데이터용)
    public List<PetWeightDTO> getWeightHistory(int petNo);
    // 3. 반려동물 상세 정보 조회 (현재/권장 체중 포함)
    public Mypet_PetDTO getPetDetails(int petNo);
}
