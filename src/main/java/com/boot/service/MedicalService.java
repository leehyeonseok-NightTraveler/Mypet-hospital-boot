package com.boot.service;

import java.util.HashMap;
import java.util.List;

import com.boot.dto.MedicalResDTO;

public interface MedicalService {

    List<MedicalResDTO> findMedicalReservations(HashMap<String, String> map);

    void createMedicalReservation(MedicalResDTO dto);
}
