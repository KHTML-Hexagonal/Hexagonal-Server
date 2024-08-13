package org.khtml.hexagonal.domain.ai.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.khtml.hexagonal.domain.ai.dto.BuildingUpdate;
import org.khtml.hexagonal.domain.ai.dto.ImageRequest;
import org.khtml.hexagonal.domain.building.application.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BuildingService buildingService;

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @PostMapping("/analyze-images/house")
    public ResponseEntity<Map<String, Object>> analyzeHouseImages(@RequestBody ImageRequest imageRequest) throws JsonProcessingException {

        // 시스템 메시지
        String systemMessage = """
                            
                            너는 오래된 건물의 상태를 평가하는 챗봇 AI다. 제공된 사진을 분석하여 건물의 상태를 진단하고, 결과를 한글로 작성된 JSON 형식으로 출력해야 한다. 
                            이 JSON은 아래 예시와 같이 각 필드가 특정 영어 변수에 매핑되도록 구성되어야 한다.
                            
                            건물 구조 분석 (structureReason):
                            
                            구조: 건물의 구조가 전통적인지 현대적인지 판단한다.
                            이유: 판단의 이유를 설명한다.
                            건축 요소 평가 (roof, walls, windowsAndDoors):
                            
                            지붕 형태 (roof):
                            재료: 지붕에 사용된 재료를 확인한다.
                            상태: 지붕의 상태를 평가한다.
                            외벽 재질 (walls):
                            재료: 외벽에 사용된 재료를 확인한다.
                            상태: 외벽의 상태를 평가한다.
                            창문 및 문 형태 (windowsAndDoors):
                            재료: 창문과 문에 사용된 재료를 확인한다.
                            상태: 창문과 문의 상태를 평가한다.
                            건물 상태 평가 (overallCondition):
                            
                            평가: 건물의 전반적인 상태를 구체적으로 평가한다.
                            이유: 해당 평가의 이유를 제시한다.
                            상세 점수화 (detailedScores):
                            
                            균열 여부 (cracks): 균열의 존재 여부와 심각성을 100점 만점으로 점수화한다.
                            누수 여부 (leaks): 누수의 존재 여부와 심각성을 100점 만점으로 점수화한다.
                            부식 여부 (corrosion): 부식의 정도를 100점 만점으로 점수화한다.
                            노후화 정도 (aging): 건물의 노후화를 100점 만점으로 점수화한다.
                            총점수 (totalScore): 위의 점수를 합산하여 반올림한 총점을 계산한다.
                            보수 필요성 판단 (repairNeeds):
                            
                            조명: 사진 속 조명이 형광등이라면 LED로 교체가 필요한지, 이미 LED라면 교체가 불필요한지 판단한다.
                            창호 보강, 도배, 장판 교체: 창호 보강, 도배, 장판 교체의 필요 여부를 판단한다.
                            
                            판단하기 어려운 부분, 예를 들어 실내 사진인데 지붕 재료, 지붕 상태 등을 판단해야 하는 경우에는 해당 부분을 ""으로 처리한다.
                            repairlist의 경우 반드시 ,로 구분한다. repairlist가 없는 경우에는 ""으로 처리한다.
                            
                            
                            예시 JSON은 다음과 같다.
                            
                            {
                              "structureReason": "구조 평가 이유",
                              "roofMaterial": "지붕 재료",
                              "roofCondition": "지붕 상태",
                              "wallMaterial": "외벽 재료",
                              "wallCondition": "외벽 상태",
                              "windowDoorMaterial": "창문 및 문 재료",
                              "windowDoorCondition": "창문 및 문 상태",
                              "overallCondition": "건물 상태 평가",
                              "conditionReason": "건물 상태 평가 이유",
                              "crackScore": 20,
                              "leakScore": 0,
                              "corrosionScore": 0,
                              "agingScore": 0,
                              "totalScore": 20,
                              "repairList": "LED 교체, 창호 보강, 도배, 장판 교체"
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
        headers.set("api-key", openAiApiKey);

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

        // responseBody에서 "content" 부분을 추출
        String content = (String) ((Map<String, Object>) ((Map<String, Object>) ((List<Object>) responseBody.get("choices")).get(0)).get("message")).get("content");

        // "```json"과 "```"을 제거하여 실제 JSON 데이터만 추출
        String jsonContent = content.replaceAll("```json\\n|```", "").trim();

        // JSON 데이터를 Map으로 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonData = objectMapper.readValue(jsonContent, new TypeReference<Map<String, Object>>() {});

        // BuildingUpdate DTO로 데이터 매핑
        BuildingUpdate buildingUpdate = new BuildingUpdate();
        buildingUpdate.setStructureReason((String) jsonData.get("structureReason"));
        buildingUpdate.setRoofMaterial((String) jsonData.get("roofMaterial"));
        buildingUpdate.setRoofCondition((String) jsonData.get("roofCondition"));
        buildingUpdate.setWallMaterial((String) jsonData.get("wallMaterial"));
        buildingUpdate.setWallCondition((String) jsonData.get("wallCondition"));
        buildingUpdate.setWindowDoorMaterial((String) jsonData.get("windowDoorMaterial"));
        buildingUpdate.setWindowDoorCondition((String) jsonData.get("windowDoorCondition"));
        buildingUpdate.setOverallCondition((String) jsonData.get("overallCondition"));
        buildingUpdate.setConditionReason((String) jsonData.get("conditionReason"));
        buildingUpdate.setCrackScore((Integer) jsonData.get("crackScore"));
        buildingUpdate.setLeakScore((Integer) jsonData.get("leakScore"));
        buildingUpdate.setCorrosionScore((Integer) jsonData.get("corrosionScore"));
        buildingUpdate.setAgingScore((Integer) jsonData.get("agingScore"));
        buildingUpdate.setTotalScore((Integer) jsonData.get("totalScore"));
        buildingUpdate.setRepairList((String) jsonData.get("repairList"));

        // 매핑된 DTO 출력 (디버깅 용도)
        System.out.println(buildingUpdate);

        //buildingService.updateBuilding(buildingId, buildingUpdate);

        // 리턴값 반환
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/analyze-images/material")
    public ResponseEntity<Map<String, Object>> analyzeMateiralImages(@RequestBody ImageRequest imageRequest) throws JsonProcessingException {

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
        headers.set("api-key", openAiApiKey);

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

        // responseBody에서 "content" 부분을 추출
        String content = (String) ((Map<String, Object>) ((Map<String, Object>) ((List<Object>) responseBody.get("choices")).get(0)).get("message")).get("content");

        // "```json"과 "```"을 제거하여 실제 JSON 데이터만 추출
        String jsonContent = content.replaceAll("```json\\n|```", "").trim();

        // JSON 데이터를 Map으로 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonData = objectMapper.readValue(jsonContent, new TypeReference<Map<String, Object>>() {});

        // 필요한 데이터 사용

        // 리턴값 반환
        return ResponseEntity.ok(responseBody);
    }
}
