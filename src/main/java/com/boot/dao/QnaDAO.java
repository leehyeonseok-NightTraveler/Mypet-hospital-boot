package com.boot.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boot.dto.Mypet_Qna_BoardDTO;

@Mapper
public interface QnaDAO {

    List<Mypet_Qna_BoardDTO> getQnaList();

    void writeQna(Mypet_Qna_BoardDTO dto);

    Mypet_Qna_BoardDTO getQnaDetail(int qna_no);

    List<Mypet_Qna_BoardDTO> list2(HashMap<String, Object> map);

    int getTotalCount2();
}
