package com.boot.service;

import java.util.HashMap;
import java.util.List;

import com.boot.dto.Mypet_NoticesDTO;

public interface NoticeService {

    List<Mypet_NoticesDTO> getNoticesList();

    Mypet_NoticesDTO getNoticeDetail(int notice_no);

    void writeNotice(Mypet_NoticesDTO dto);

    void modifyNotice(Mypet_NoticesDTO dto);

    void deleteNotice(int notice_no);

    void increaseNoticeViewCount(int notice_no);

    List<Mypet_NoticesDTO> list(HashMap<String, Object> map);

    int getTotalCount();
}
