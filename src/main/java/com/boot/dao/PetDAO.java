package com.boot.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boot.dto.Mypet_PetDTO;

@Mapper
public interface PetDAO {

    List<Mypet_PetDTO> selectPetsByUserNo(HashMap<String, Object> map);

    Mypet_PetDTO getPetInfo(HashMap<String, Object> map);

    void petjoin(Mypet_PetDTO petDTO);

    int updatePetInfo(HashMap<String, Object> map);

    int checkDuplicatePetImage(String img_temp);
    void updatePetImage(HashMap<String, Object> map);

    Mypet_PetDTO getPetByNo(int pet_no);
}
