package com.boot.service;

import com.boot.dto.Mypet_PetDTO;
import com.boot.dto.Mypet_UserDTO;

import java.util.List;

public interface ManageService {
    List<Mypet_UserDTO> UserList();
    Mypet_UserDTO UserInfo(int user_no);
    List<Mypet_PetDTO> PetList(int user_no);
}
