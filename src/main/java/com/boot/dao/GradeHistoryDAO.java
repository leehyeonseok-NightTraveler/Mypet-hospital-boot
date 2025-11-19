package com.boot.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.boot.dto.GradeHistoryDTO;

@Mapper
public interface GradeHistoryDAO {
    List<GradeHistoryDTO> getGradeHistory(@Param("user_no") int userNo);
}
