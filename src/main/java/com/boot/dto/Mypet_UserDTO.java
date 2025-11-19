package com.boot.dto;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mypet_UserDTO {
    private int user_no;           // 회원 고유번호
    private String user_id;        // 로그인 ID
    private String user_pwd;       // 비밀번호
    private String user_name;      // 이름
    private String user_gender;    // 성별
    private Date user_birthday;    // 생일
    private String user_phone;     // 전화번호
    private String user_email;     // 이메일
    private Date user_regidate;    // 가입일 (Date로 수정)
    private String user_addr;      // 주소
    private String user_addr_detail;
    private String user_status;    // 상태
    private String user_img;       // 프로필 이미지 URL
    private String user_img_temp;  // 이미지 해시값 (중복 방지용)
    private String social_type; // "kakao""google"
    private String social_id;   // 카카오가 제공하는 고유 ID
    private String current_grade;   // 현재 등급
    private Date grade_expiry_date; // 등급 만료일


    private List<Mypet_PetDTO> pets;  // 유저가 보유한 펫 목록 (연동용)
}
