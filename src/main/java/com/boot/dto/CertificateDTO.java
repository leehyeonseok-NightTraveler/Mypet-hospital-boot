package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDTO {
    private int service_no;
    private Date service_date;
    private String service_type;
    private String service_item;
    private String details_memo;

    // UserInfo 정보
    private int user_no;
    private String user_name;
    private int user_age;
    private String user_addr;
    private String user_addr_detail;

    // PetInfo 정보
    private int pet_no;
    private String pet_name;
    private String pet_gender;
    private int pet_age;
    private String pet_species;
    private String pet_breed;
}
