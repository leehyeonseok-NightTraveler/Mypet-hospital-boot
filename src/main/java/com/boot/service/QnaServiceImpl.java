package com.boot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boot.dto.Mypet_Qna_ReplyDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    SqlSession sqlSession;

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

    @Override
    public Mypet_Qna_ReplyDTO getQnaReply(int qna_no) {
        QnaDAO dao = sqlSession.getMapper(QnaDAO.class);
        return dao.getQnaReply(qna_no);
    }

    @Override
    public void writeReply(Map<String, Object> params) {
        QnaDAO dao = sqlSession.getMapper(QnaDAO.class);
        dao.writeReply(params);
    }

    @Override
    public void modifyReply(Map<String, Object> params) {
        QnaDAO dao = sqlSession.getMapper(QnaDAO.class);
        dao.modifyReply(params);
    }

    @Override
    public void deleteQna(int qna_no) {
        QnaDAO dao = sqlSession.getMapper(QnaDAO.class);
        dao.deleteQna(qna_no);
    }

    @Override
    public void deleteReplyByQnaNo(int qna_no) {
        QnaDAO dao = sqlSession.getMapper(QnaDAO.class);
        dao.deleteReplyByQnaNo(qna_no);
    }

    @Override
    public void qnaStatusUpdate(int qna_no) {
        QnaDAO dao = sqlSession.getMapper(QnaDAO.class);
        dao.qnaStatusUpdate(qna_no);
    }
}
