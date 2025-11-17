package com.boot.service;

import com.boot.dao.ManageDAO;
import com.boot.dto.*;
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
    public List<Mypet_UserDTO> UserList(Criteria cri) {
       ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
       return dao.UserList(cri);
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
    public List<MedicalResDTO> VeterinaryResList(Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.VeterinaryResList(cri);
    }

    @Override
    public List<GroomingResDTO> GroomingResList(Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.GroomingResList(cri);
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
    public int getUserTotal(Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.getUserTotal(cri);
    }

    @Override
    public int getVetResTotal(Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.getVetResTotal(cri);
    }

    @Override
    public int getGroResTotal(Criteria cri) {
        ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
        return dao.getGroResTotal(cri);
    }


}
