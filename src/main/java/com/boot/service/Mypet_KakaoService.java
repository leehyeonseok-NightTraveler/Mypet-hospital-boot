package com.boot.service;

import com.boot.dto.Mypet_UserDTO;

public interface Mypet_KakaoService {
    String getKakaoLoginURL();
    String getKakaoAccessToken(String code);
    Mypet_UserDTO getKakaoUserInfo(String accessToken);
    Mypet_UserDTO findUserBySocialId(String socialId);
    void socialJoin(Mypet_UserDTO userDTO);
}