package com.boot.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boot.dto.MedicalResDTO;

@Mapper
public interface MedicalDAO {

    List<MedicalResDTO> findMedicalReservations(HashMap<String, String> map);

    void createMedicalReservation(MedicalResDTO dto);
}
