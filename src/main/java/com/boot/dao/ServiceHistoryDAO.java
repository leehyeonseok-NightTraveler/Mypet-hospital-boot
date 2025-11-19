package com.boot.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.boot.dto.ServiceHistoryDTO;

@Mapper
public interface ServiceHistoryDAO {
    List<ServiceHistoryDTO> getServiceHistory(@Param("user_no") int userNo);
}
