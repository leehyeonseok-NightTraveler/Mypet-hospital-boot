package com.boot.dao;

import com.boot.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ManageDAO {
    List<Mypet_UserDTO> UserList(@Param("params") Map<String, Object> params,
                                 @Param("cri") Criteria cri);

    Mypet_UserDTO UserInfo(int user_no);

    List<Mypet_PetDTO> PetList(int user_no);

    List<MedicalResDTO> VeterinaryResList(@Param("params") Map<String, Object> params,
                                          @Param("cri") Criteria cri);

    List<GroomingResDTO> GroomingResList(@Param("params") Map<String, Object> params,
                                         @Param("cri") Criteria cri);

    void confirmRes(@Param("params") Map<String, Object> params);

    void cancelRes(@Param("params") Map<String, Object> params);

    int getUserTotal(@Param("params") Map<String, Object> params,
                     @Param("cri") Criteria cri);

    int getVetResTotal(@Param("params") Map<String, Object> params,
                       @Param("cri") Criteria cri);

    int getGroResTotal(@Param("params") Map<String, Object> params,
                       @Param("cri") Criteria cri);

    void UserStatusProcess(@Param("params") Map<String, Object> params);

    List<GradeHistoryDTO> getGradeHistory(int user_no);

    List<ServiceHistoryDTO> getServiceHistory(int user_no);

    void insertServiceHistory(ServiceHistoryDTO dto);

    void completeService(int service_no);

    List<Mypet_PetDTO> getPetList(int user_no);

    List<CertificateDTO> getCertificate(@Param("params") Map<String, Object> params);
}
