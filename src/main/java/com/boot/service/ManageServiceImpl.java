package com.boot.service;

import com.boot.dao.ManageDAO;
import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.Mypet_UserDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<Mypet_UserDTO> UserList() {
       ManageDAO dao = sqlSession.getMapper(ManageDAO.class);
       return dao.UserList();
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
}
