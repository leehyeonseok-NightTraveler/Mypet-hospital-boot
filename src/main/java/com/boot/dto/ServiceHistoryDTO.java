package com.boot.dto;

import java.sql.Date;
import lombok.Data;

@Data
public class ServiceHistoryDTO {
    private int service_no;
    private int user_no;
    private int pet_no;
    private Date service_date;
    private String service_type;
    private String service_item;
    private String details_memo;
    private String completion_status;
    private Date completion_date;
    private String pet_name;
    private String pet_breed;
}
