package com.boot.dto;

import java.sql.Date;
import lombok.Data;

@Data
public class GradeHistoryDTO {
    private int history_no;
    private int user_no;
    private String grade;
    private Date start_date;
    private Date end_date;
    private int evaluation_visits;
    private Date evaluation_date;
}
