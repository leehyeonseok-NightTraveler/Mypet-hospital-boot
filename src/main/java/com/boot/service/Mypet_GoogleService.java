package com.boot.service;

import com.boot.dto.Mypet_UserDTO;

public interface Mypet_GoogleService {
    
    String getGoogleLoginURL();
    
    String getGoogleAccessToken(String code);
    
    Mypet_UserDTO getGoogleUserInfo(String accessToken);
}