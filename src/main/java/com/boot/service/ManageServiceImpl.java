package com.boot.service;

import com.boot.dao.ManageDAO;
import com.boot.dto.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<Mypet_UserDTO> UserList(Map<String, Object> params, Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.UserList(params, cri);
    }

    @Override
    public Mypet_UserDTO UserInfo(int user_no) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.UserInfo(user_no);
    }

    @Override
    public List<Mypet_PetDTO> PetList(int user_no) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.PetList(user_no);
    }

    @Override
    public List<MedicalResDTO> VeterinaryResList(Map<String, Object> params, Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.VeterinaryResList(params, cri);
    }

    @Override
    public List<GroomingResDTO> GroomingResList(Map<String, Object> params, Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.GroomingResList(params, cri);
    }

    @Override
    public void confirmRes(Map<String, Object> params) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        dao.confirmRes(params);
    }

    @Override
    public void cancelRes(Map<String, Object> params) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        dao.cancelRes(params);
    }

    @Override
    public int getUserTotal(Map<String, Object> params, Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.getUserTotal(params, cri);
    }

    @Override
    public int getVetResTotal(Map<String, Object> params, Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.getVetResTotal(params, cri);
    }

    @Override
    public int getGroResTotal(Map<String, Object> params, Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.getGroResTotal(params, cri);
    }

    @Override
    public void UserStatusProcess(Map<String, Object> params) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        dao.UserStatusProcess(params);
    }

    @Override
    public List<GradeHistoryDTO> getGradeHistory(int user_no) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.getGradeHistory(user_no);
    }

    @Override
    public List<ServiceHistoryDTO> getServiceHistory(int user_no) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.getServiceHistory(user_no);
    }

    @Override
    public void insertServiceHistory(ServiceHistoryDTO dto) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        dao.insertServiceHistory(dto);
    }

    @Override
    public void completeService(int service_no) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        dao.completeService(service_no);
    }

    @Override
    public List<Mypet_PetDTO> getPetList(int user_no) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.PetList(user_no);
    }

    @Override
    public List<CertificateDTO> getCertificate(Map<String, Object> params) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.getCertificate(params);
    }
}
