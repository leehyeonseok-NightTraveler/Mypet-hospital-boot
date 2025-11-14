package com.boot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeywordDTO {
	 private String keyword;
	 
	 @JsonProperty("siteurl")
	 private String siteurl; // DB 컬럼명과 동일해야 함
}
