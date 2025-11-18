package com.boot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boot.dto.Mypet_Qna_BoardDTO;
import com.boot.dto.Mypet_Qna_ReplyDTO;
import org.apache.ibatis.annotations.Param;

public interface QnaService {

    List<Mypet_Qna_BoardDTO> getQnaList();

    void writeQna(Mypet_Qna_BoardDTO dto);

    Mypet_Qna_BoardDTO getQnaDetail(int qna_no);

    List<Mypet_Qna_BoardDTO> list2(HashMap<String, Object> map);

    int getTotalCount2();

    Mypet_Qna_ReplyDTO getQnaReply(int qna_no);

    void writeReply (Map<String, Object> params);

    void modifyReply (Map<String, Object> params);

    void deleteQna(int qna_no);

    void deleteReplyByQnaNo(int qna_no);

    void qnaStatusUpdate(int qna_no);

}
