package com.boot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.dao.MedicalDAO;
import com.boot.dto.MedicalResDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalServiceImpl implements MedicalService {

    private final MedicalDAO medicalDAO;

    @Override
    public List<MedicalResDTO> findMedicalReservations(HashMap<String, String> map) {
        return medicalDAO.findMedicalReservations(map);
    }

    @Override
    @Transactional
    public void createMedicalReservation(MedicalResDTO dto) {
        medicalDAO.createMedicalReservation(dto);
    }
}
