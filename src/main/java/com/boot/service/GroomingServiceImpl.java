package com.boot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.dao.GroomingDAO;
import com.boot.dto.GroomingResDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroomingServiceImpl implements GroomingService {

    private final GroomingDAO groomingDAO;

    @Override
    public List<GroomingResDTO> findGroomingReservations(HashMap<String, String> map) {
        return groomingDAO.findGroomingReservations(map);
    }

    @Override
    @Transactional
    public void createGroomingReservation(GroomingResDTO dto) {
        groomingDAO.createGroomingReservation(dto);
    }
}
