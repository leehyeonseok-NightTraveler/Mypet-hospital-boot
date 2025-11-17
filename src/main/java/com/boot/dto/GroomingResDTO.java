package com.boot.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

@Data
public class GroomingResDTO {
    private int res_no;
    private String user_name;
    private int user_no;
    private String pet_name;
    private int pet_no;
    private String user_phone;
    private String service_item;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date res_date;
    private String res_status;
    private Date reg_date;
    private String memo;
    private String pet_breed;
    private String  cancel_reason;
}
