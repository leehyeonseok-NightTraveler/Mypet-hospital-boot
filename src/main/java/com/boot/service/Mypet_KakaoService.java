package com.boot.service;

import com.boot.dto.Mypet_UserDTO;

public interface Mypet_KakaoService {
    
    String getKakaoLoginURL();
    
    String getKakaoAccessToken(String code);
    
    Mypet_UserDTO getKakaoUserInfo(String accessToken);
    
    Mypet_UserDTO findUserBySocialId(String socialId);
    
    // (INSERT용)
    void socialJoin_withDetails(Mypet_UserDTO userDTO); 
    
    // 🔻🔻 (UPDATE용) 메소드 추가 🔻🔻
    void socialUpdate_withDetails(Mypet_UserDTO userDTO);
}