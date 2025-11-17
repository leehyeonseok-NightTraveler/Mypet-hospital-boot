package com.boot.dto;


import lombok.AllArgsConstructor;
import lombok.Data; 

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class Criteria {
	private int pageNum;
	private int amount;
	
	public Criteria() {
		this(1,10);
	}
}
