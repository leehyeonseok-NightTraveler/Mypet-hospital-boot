package com.boot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NaverUserInfoDTO {
	@JsonProperty("resultcode")
    private String resultCode;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("response") // ⭐️ 실제 사용자 정보가 담긴 객체
    private NaverProfile response;

    // Getter
    public String getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public NaverProfile getResponse() {
        return response;
    }
}
