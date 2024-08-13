package org.khtml.hexagonal.domain.ai.presentation;

import org.khtml.hexagonal.domain.ai.dto.ImageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GptController {

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @PostMapping("/analyze-images/house")
    public ResponseEntity<Map<String, Object>> analyzeHouseImages(@RequestBody ImageRequest imageRequest) {

        // 시스템 메시지
        String systemMessage = """
                    
                            프롬프트 개선 버전:
                            
                            너는 부식된 건물의 상태를 평가하는 챗봇 AI다. 제공된 사진을 분석하여 건물의 상태를 진단하고, 결과를 한글로 작성된 JSON 형식으로 출력해야 한다.
                            
                            다음과 같은 절차를 따른다:
                            
                            사진 개수 확인: 사진이 몇 장 있는지 확인한 후, 먼저 사진 개수를 한글로 말해줘.
                            
                            건물 구조 분석:
                            
                            건물의 구조가 전통적인 방식인지 현대적인지, 양호한지 판별한다.
                            판단한 이유도 함께 설명해줘.
                            건축 요소 평가:
                            
                            지붕 형태: 슬레이트, 기와, 함석 등 어떤 재료가 사용되었는지 판단하고 상태를 평가해.
                            외벽 재질: 벽돌, 콘크리트, 목재 등 사용된 재료를 확인하고 상태를 평가해.
                            창문 및 문 형태: 알루미늄 섀시, 목재 문, 철문 등 재질과 상태를 분석해.
                            건물 상태 평가:
                            
                            건물의 전반적인 상태를 구체적으로 평가하고, 그 이유를 제시해.
                            상세 점수화:
                            
                            균열 여부, 누수 여부, 부식 여부, 노후화 정도를 각각 100점 만점으로 점수화해.
                            보수 필요성 판단:
                            
                            사진 속 등이 형광등이라면 LED로 교체가 필요한지, 이미 LED라면 교체가 불필요한지 판단해.
                            창호 보강, 도배, 장판 교체의 필요 여부도 함께 판단해줘.
                            
                            결과는 아래 예시와 같은 JSON 형식으로 출력해야 한다:
                            
                            {
                                "사진 개수": "두 장",
                                "균열 여부": 85,
                                "누수 여부": 75,
                                "부식 여부": 65,
                                "노후화 정도": 55,
                                "총점수": 70,
                                "보수가 필요한 목록": ["창문", "창호", "LED"]
                            }
                            
                """;

        // OpenAI API에 보낼 요청 데이터 작성
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> messages = new ArrayList<>();

        // System 메시지 추가
        Map<String, Object> systemContent = new HashMap<>();
        systemContent.put("type", "text");
        systemContent.put("text", systemMessage);
        messages.add(Map.of("role", "system", "content", List.of(systemContent)));

        // User 메시지 추가 (여러 이미지 URL 포함)
        List<Map<String, Object>> imageUrls = new ArrayList<>();
        imageRequest.getContent().forEach(content -> {
            String url = content.getImageUrlOrNull();
            if (url != null) {
                Map<String, Object> imageUrlContent = new HashMap<>();
                imageUrlContent.put("type", "image_url");
                imageUrlContent.put("image_url", Map.of("url", url));
                imageUrls.add(imageUrlContent);
            }
        });
        messages.add(Map.of("role", "user", "content", imageUrls));

        // Assistant 메시지 추가 (빈 값으로 초기화)
        Map<String, Object> assistantContent = new HashMap<>();
        assistantContent.put("type", "text");
        assistantContent.put("text", "");
        messages.add(Map.of("role", "assistant", "content", List.of(assistantContent)));

        // 요청 본문에 messages 리스트와 옵션 추가
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("top_p", 0.95);
        requestBody.put("max_tokens", 2000);

        // API 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", openAiApiKey); // "api-key"로 API 키 설정

        // HTTP 요청 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // REST API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                openAiApiUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // API 결과 처리
        Map<String, Object> responseBody = response.getBody();

        // 리턴값 반환
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/analyze-images/material")
    public ResponseEntity<Map<String, Object>> analyzeMateiralImages(@RequestBody ImageRequest imageRequest) {

        // 시스템 메시지
        String systemMessage = """
            blah blah blah
        """;

        // OpenAI API에 보낼 요청 데이터 작성
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> messages = new ArrayList<>();

        // System 메시지 추가
        Map<String, Object> systemContent = new HashMap<>();
        systemContent.put("type", "text");
        systemContent.put("text", systemMessage);
        messages.add(Map.of("role", "system", "content", List.of(systemContent)));

        // User 메시지 추가 (여러 이미지 URL 포함)
        List<Map<String, Object>> imageUrls = new ArrayList<>();
        imageRequest.getContent().forEach(content -> {
            String url = content.getImageUrlOrNull();
            if (url != null) {
                Map<String, Object> imageUrlContent = new HashMap<>();
                imageUrlContent.put("type", "image_url");
                imageUrlContent.put("image_url", Map.of("url", url));
                imageUrls.add(imageUrlContent);
            }
        });
        messages.add(Map.of("role", "user", "content", imageUrls));

        // Assistant 메시지 추가 (빈 값으로 초기화)
        Map<String, Object> assistantContent = new HashMap<>();
        assistantContent.put("type", "text");
        assistantContent.put("text", "");
        messages.add(Map.of("role", "assistant", "content", List.of(assistantContent)));

        // 요청 본문에 messages 리스트와 옵션 추가
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("top_p", 0.95);
        requestBody.put("max_tokens", 2000);

        // API 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", openAiApiKey); // "api-key"로 API 키 설정

        // HTTP 요청 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // REST API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                openAiApiUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // API 결과 처리
        Map<String, Object> responseBody = response.getBody();

        // 리턴값 반환
        return ResponseEntity.ok(responseBody);
    }
}
