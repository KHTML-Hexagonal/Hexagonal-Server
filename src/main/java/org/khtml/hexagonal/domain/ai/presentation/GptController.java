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

    @PostMapping("/analyze-images")
    public ResponseEntity<Map<String, Object>> analyzeImages(@RequestBody ImageRequest imageRequest) {

        // 시스템 메시지
        String systemMessage = """
            여기 사진 몇 개인지 확인한 다음 개수 먼저 말해. 반드시 한글로 대답해야 하고 답변은 마지막에 나올 양식을 따라야 해. 너는 부식된 건물의 상태를 판단하는 챗봇 AI야.

            사진을 입력받으면 해당 사진을 분석해서 다음과 같은 정보들을 분석해내야 해.

            1. 건물의 구조
            건물의 구조가 옛날 방식인지 양호한지 판가름하고 그 이유에 대해서 알려줘.

            2. 지붕의 형태: 슬레이트, 기와, 함석 등,
            외벽의 재질: 벽돌, 콘크리트, 목재 등,
            창문 및 문의 형태: 알루미늄 섀시, 목재 문, 철문 등
            어느 것을 사용했고 현재 상태가 어떤지 평가해줘.

            3. 건물의 상태는 어떤지 구체적으로 평가하고 그 이유를 제시해줘.

            4. 균열 여부, 누수 여부, 부식 여부, 노후화 정도를 각각 100점 만점으로 점수를 매겨줘.

            그리고 판단해야 하는 기준이 있는데,

            사진 속 등이 형광등이라면 LED 등기구로 교체 필요/ 이미 LED 등기구라면 교체 불필요, 창호 보강 필요/불필요, 도배 필요/불필요, 장판 교체 필요/불필요 등에 대해 판단해줘야 해.

            return은 json 형식으로 되어있어야 하는데 예시는 다음과 같아.

            Output:

            {
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
        requestBody.put("max_tokens", 800);

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
