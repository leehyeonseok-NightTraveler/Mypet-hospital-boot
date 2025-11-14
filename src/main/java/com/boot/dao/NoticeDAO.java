package com.boot.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boot.dto.Mypet_NoticesDTO;

@Mapper
public interface NoticeDAO {

    List<Mypet_NoticesDTO> selectNotices();

    Mypet_NoticesDTO selectNoticeByNo(int notice_no);

    void insertNotice(Mypet_NoticesDTO dto);

    void updateNotice(Mypet_NoticesDTO dto);

    void increaseViewCount(int notice_no);

    void deleteNotice(int notice_no);

    List<Mypet_NoticesDTO> list(HashMap<String, Object> map);

    int getTotalCount();
}
