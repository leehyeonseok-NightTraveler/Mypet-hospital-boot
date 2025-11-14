package com.boot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.boot.dao.NoticeDAO;
import com.boot.dto.Mypet_NoticesDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeDAO noticeDAO;

    @Override
    public List<Mypet_NoticesDTO> getNoticesList() {
        return noticeDAO.selectNotices();
    }

    @Override
    public Mypet_NoticesDTO getNoticeDetail(int notice_no) {
        noticeDAO.increaseViewCount(notice_no);
        return noticeDAO.selectNoticeByNo(notice_no);
    }

    @Override
    public void writeNotice(Mypet_NoticesDTO dto) {
        noticeDAO.insertNotice(dto);
    }

    @Override
    public void modifyNotice(Mypet_NoticesDTO dto) {
        noticeDAO.updateNotice(dto);
    }

    @Override
    public void deleteNotice(int notice_no) {
        noticeDAO.deleteNotice(notice_no);
    }

    @Override
    public void increaseNoticeViewCount(int notice_no) {
        noticeDAO.increaseViewCount(notice_no);
    }

    @Override
    public List<Mypet_NoticesDTO> list(HashMap<String, Object> map) {
        return noticeDAO.list(map);
    }

    @Override
    public int getTotalCount() {
        return noticeDAO.getTotalCount();
    }
}
