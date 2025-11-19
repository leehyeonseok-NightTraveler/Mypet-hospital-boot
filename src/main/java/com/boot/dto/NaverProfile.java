package com.boot.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class NaverProfile {
	private String id;
    private String email;
    private String name;
    private String profile_image;
}
