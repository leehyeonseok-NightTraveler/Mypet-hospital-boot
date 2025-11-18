package com.boot.dto;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mypet_PetDTO {
	  private int pet_no;            // 펫 고유번호
	    private int user_no;           // 보호자 번호
	    private String pet_name;       // 펫 이름
	    private int pet_age;           // 나이
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date pet_birthday;   // 생년월일
	    private String pet_gender;     // 성별
	    private String pet_species;    // 종
	    private String pet_breed;      // 품종
	    private Date pet_regdate;    // 등록일
	    private String pet_neutered;   // 중성화 여부
	    private String pet_haschip;    // 칩 등록 여부
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date pet_chip_regdate; // 칩 등록일
	    private String pet_img;        // 이미지 경로
	    private String pet_img_temp;   // 이미지 해시
	    private double recommended_weight;
	    private double current_weight;
}
