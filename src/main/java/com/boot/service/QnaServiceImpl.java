package com.boot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.dao.QnaDAO;
import com.boot.dto.Mypet_Qna_BoardDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {

    private final QnaDAO qnaDAO;

    @Override
    public List<Mypet_Qna_BoardDTO> getQnaList() {
        return qnaDAO.getQnaList();
    }

    @Override
    @Transactional
    public void writeQna(Mypet_Qna_BoardDTO dto) {
        qnaDAO.writeQna(dto);
    }

    @Override
    public Mypet_Qna_BoardDTO getQnaDetail(int qna_no) {
        return qnaDAO.getQnaDetail(qna_no);
    }

    @Override
    public List<Mypet_Qna_BoardDTO> list2(HashMap<String, Object> map) {
        return qnaDAO.list2(map);
    }

    @Override
    public int getTotalCount2() {
        return qnaDAO.getTotalCount2();
    }
}
