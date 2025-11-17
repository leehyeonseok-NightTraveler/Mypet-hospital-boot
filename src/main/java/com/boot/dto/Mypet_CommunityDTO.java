package com.boot.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mypet_CommunityDTO {
	private int post_no;
	private int user_no;
	private String user_name;
	private String post_title;
	private String post_content;
	private String post_file;
	private int view_count;
	private Date created_date;
	private Date updated_date;
}
