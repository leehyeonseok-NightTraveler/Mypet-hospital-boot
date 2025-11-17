package com.boot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.boot.dto.Mypet_PetDTO;

public interface PetService {

    Mypet_PetDTO getPetInfo(int user_no, int pet_no);

    List<Mypet_PetDTO> getPetsByUserNo(int user_no);

    void petjoin(Mypet_PetDTO petDTO);

    void updatePetInfo(HashMap<String, Object> map);

    Mypet_PetDTO getPetByNo(int pet_no);
    
    void updatePetImg(int pet_no, String imgPath, String imgHash);
    void replacePetImage(int petNo, MultipartFile file);
}
