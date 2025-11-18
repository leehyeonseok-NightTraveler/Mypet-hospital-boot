package com.boot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.boot.dao.PetDAO;
import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.PetWeightDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetDAO petDAO;
    
    private final UploadService uploadService;
    
    @Autowired
    private SqlSessionTemplate sqlSession;

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
    public void updatePetImg(int pet_no, String imgPath, String imgHash) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("pet_no", pet_no);
        map.put("pet_img", imgPath);
        map.put("pet_img_temp", imgHash);

        petDAO.updatePetImage(map);
    }
    
    @Override
    public void replacePetImage(int petNo, MultipartFile file) {

        // 1) 기존 이미지 조회
        Mypet_PetDTO pet = petDAO.getPetByNo(petNo);
        String oldImg = pet.getPet_img();   // 기존 이미지 경로

        try {
            // 2) 새 이미지 저장 (인스턴스 uploadService 사용)
            String saved = uploadService.saveImageWithHash(file, "pet");

            // 3) 해시 추출
            String fileName = saved.substring(saved.lastIndexOf("/") + 1);
            String hash = fileName.split("_")[1];

            // 4) DB 업데이트
            HashMap<String, Object> map = new HashMap<>();
            map.put("pet_no", petNo);
            map.put("pet_img", saved);
            map.put("pet_img_temp", hash);
            petDAO.updatePetImage(map);

            // 5) 기존 파일 자동 삭제 (마찬가지로 인스턴스 호출)
            if (oldImg != null && !oldImg.isEmpty()) {
                uploadService.deleteFile(oldImg);
            }

        } catch (Exception e) {
            log.error("펫 이미지 교체 오류", e);
            throw new RuntimeException("펫 이미지 변경 실패");
        }
    }

	@Override
	public void addPetWeight(int petNo, double weight) {
		Map<String, Object> params = new HashMap<>();
        params.put("pet_no", petNo);
        params.put("weight_kg", weight);
        
        // ⭐️ Map을 파라미터로 넘깁니다.
        sqlSession.insert("com.boot.dao.PetMapper.insertWeight", params);
	}

	@Override
	public List<PetWeightDTO> getWeightHistory(int petNo) {
		return sqlSession.selectList("com.boot.dao.PetMapper.getWeightHistory", petNo);
	}

	@Override
	public Mypet_PetDTO getPetDetails(int petNo) {
		Mypet_PetDTO pet = sqlSession.selectOne("com.boot.dao.PetMapper.getPetByNo", petNo);
        
        if (pet != null) {
            Double latestWeight = sqlSession.selectOne("com.boot.dao.PetMapper.getLatestWeight", petNo);
            pet.setCurrent_weight(latestWeight != null ? latestWeight : 0); 
        }
        
        return pet;
       
    }

}
