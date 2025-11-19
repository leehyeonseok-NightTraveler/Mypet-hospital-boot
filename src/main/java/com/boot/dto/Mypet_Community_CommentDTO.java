package com.boot.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mypet_Community_CommentDTO {
	private int comment_no;
	private int post_no;
	private int user_no;
	private String user_name;
	private String comment_content;
	private String created_at2;
}
