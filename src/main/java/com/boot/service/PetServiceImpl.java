package com.boot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.dao.PetDAO;
import com.boot.dto.Mypet_PetDTO;
import com.boot.util.ImageHashUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetDAO petDAO;

    @Override
    public Mypet_PetDTO getPetInfo(int user_no, int pet_no) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_no", user_no);
        map.put("pet_no", pet_no);
        return petDAO.getPetInfo(map);
    }

    @Override
    public List<Mypet_PetDTO> getPetsByUserNo(int user_no) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_no", user_no);
        return petDAO.selectPetsByUserNo(map);
    }

    @Override
    @Transactional
    public void petjoin(Mypet_PetDTO petDTO) {
        petDAO.petjoin(petDTO);
    }

    @Override
    public void updatePetInfo(HashMap<String, Object> map) {
        petDAO.updatePetInfo(map);
    }

    @Override
    public Mypet_PetDTO getPetByNo(int pet_no) {
        return petDAO.getPetByNo(pet_no);
    }

    @Override
    public boolean uploadPetImage(int pet_no, String fileName, byte[] bytes) {
        try {
            String hash = ImageHashUtil.getReadableHash(bytes);
            if (petDAO.checkDuplicatePetImage(hash) > 0) return false;

            HashMap<String, Object> map = new HashMap<>();
            map.put("pet_no", pet_no);
            map.put("pet_img", "/resources/upload/pet/" + fileName);
            map.put("pet_img_temp", hash);

            petDAO.updatePetImage(map);

            return true;

        } catch (Exception e) {
            log.error("펫 이미지 업로드 중 오류", e);
            return false;
        }
    }
}
