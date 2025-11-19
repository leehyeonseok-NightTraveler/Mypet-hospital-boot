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
        
        // 1. ✅ answer 변수를 try 블록 밖에서 선언하고 null로 초기화합니다.
        String answer = null; 

        String prompt = """
                당신은 MY PET 동물병원의 친절한 챗봇입니다. 다음 규칙을 정확히 지켜주세요:

                - 답변은 무조건 한국어로만 해주세요
                - 예약 관련 질문이 나오면 반드시 아래 링크를 클릭 가능한 형태로 안내하세요:
                  <a href="http://localhost:8686/reservation" target="_blank" style="color:#0066cc; font-weight:bold; text-decoration:underline;">예약 페이지 바로가기</a>
                - 링크는 절대 그냥 텍스트로 쓰지 말고 반드시 위 HTML 코드 그대로 사용하세요
                - 답변은 2~3문장 정도로 간결하고 따뜻하게, 이모지는 적당히
                - 운영시간: 평일 10:00 ~ 19:00 (주말/공휴일 휴무)

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

            // 응답에서 텍스트 안전하게 추출
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) resBody.get("candidates");
            Map<String, Object> candidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) candidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            Map<String, Object> part = parts.get(0);
            
            // 2. ✅ 선언된 answer 변수에 값 할당
            answer = (String) part.get("text");

            // 3. ✅ 정상 처리된 경우, 여기서 응답 반환
            return Map.of("response", answer.trim()); 

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // 4xx 에러 (Client side error, API Key 틀림, 권한 없음 등)
            System.err.println("Gemini API Error - Status: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
            return Map.of("response", "API 연결 오류: 코드를 확인해 주세요.");

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("response", "죄송해요, 지금은 잠시 응답이 어려워요. 잠시 후 다시 시도해주세요.");
        }
        
        // 4. ❌ 이 코드는 도달할 수 없으므로 제거됩니다. (try 블록 내부에서 이미 return 됨)
        // return Map.of("response", answer.trim()); 
    }
}