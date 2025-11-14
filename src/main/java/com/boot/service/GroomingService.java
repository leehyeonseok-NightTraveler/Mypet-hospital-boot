package com.boot.service;

import java.util.HashMap;
import java.util.List;

import com.boot.dto.GroomingResDTO;

public interface GroomingService {

    List<GroomingResDTO> findGroomingReservations(HashMap<String, String> map);

    void createGroomingReservation(GroomingResDTO dto);
}
