package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDTO {
	private String title;   // 캘린더에 표시될 제목 (예: "심장사상충 예방", "전체 미용")
    private String start;   // 시작 날짜 (YYYY-MM-DD 형식)
    private String color;   // 이벤트 색상 (진료: 빨강, 미용: 파랑 등)
    private String url;     // (선택) 클릭 시 이동할 상세페이지 주소
}
