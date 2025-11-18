package com.boot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
public class ChatBotController {

    @Value("${gemini.api.url}")
    private String geminiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> req) {
        String query = req.get("query");

        String prompt = """
        	    당신은 MY PET 동물병원의 친절하고 똑똑한 챗봇 상담사예요.
        	    다음 규칙을 잘 지켜주세요:

        	    - 예약, 진료시간, 진료과목, 위치, 접수, 비용 등 병원에 관한 모든 질문은 최대한 정확하고 친절하게 답변해주세요
        	    - 예약은 http://localhost:8686/reservation 에서 가능하다고 꼭 안내해주세요
        	    - 운영시간은 평일 오전 10시 ~ 오후 7시, 주말·공휴일은 쉽니다
        	    - 인사말("안녕", "반가워" 등)은 "안녕하세요! MY PET 동물병원입니다. 어떤 도움을 드릴까요?"처럼 따뜻하게 답변
        	    - 완전히 병원과 상관없는 질문(날씨, 정치, 주식 등)은 "죄송해요, 저는 병원 관련 문의만 도와드리고 있어요"라고만 답변
        	    - 답변은 2~3문장 정도로 간결하게, 이모지 적당히 넣어서 따뜻한 느낌으로 해주세요

        	    사용자 질문: %s
        	    """.formatted(query);

        try {
            Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                    "parts", List.of(Map.of("text", prompt))
                ))
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(geminiUrl, HttpMethod.POST, entity, Map.class);
            Map<String, Object> resBody = response.getBody();

            if (resBody == null || !resBody.containsKey("candidates")) {
                return Map.of("response", "죄송해요, 응답을 받지 못했어요. 다시 시도해주세요.");
            }

            // === 수정된 부분 시작 (안전하게 꺼내기) ===
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) resBody.get("candidates");
            Map<String, Object> candidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) candidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            Map<String, Object> part = parts.get(0);
            String answer = (String) part.get("text");
            // === 수정된 부분 끝 ===

            return Map.of("response", answer.trim());

        } catch (Exception e) {
            e.printStackTrace();  // 콘솔에 오류 출력 (디버깅용)
            return Map.of("response", "죄송해요, 지금은 잠시 응답이 어려워요. 잠시 후 다시 시도해주세요.");
        }
    }
}