package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class Criteria {
	private int pageNum;
	private int amount;
	private String type;
	private String keyword;
	
	public Criteria() {
		this(1,10);
	}
	
	public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
    }

    // MyBatis에서 LIMIT 용으로 필요한 메서드
    public int getStartRow() {
        return (pageNum - 1) * amount;
    }
    
    public int getOffset() {
        return (pageNum - 1) * amount;
    }
}
