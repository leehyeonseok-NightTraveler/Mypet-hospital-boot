package com.boot.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boot.dto.GroomingResDTO;

@Mapper
public interface GroomingDAO {

    List<GroomingResDTO> findGroomingReservations(HashMap<String, String> map);

    void createGroomingReservation(GroomingResDTO dto);
}
