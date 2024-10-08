package org.khtml.hexagonal.domain.ai.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.building.dto.BuildingUpdate;
import org.khtml.hexagonal.domain.building.dto.ImageRequest;
import org.khtml.hexagonal.domain.building.dto.MaterialInfo;
import org.khtml.hexagonal.domain.building.dto.MaterialResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class GptManager {

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public BuildingUpdate analyzeBuilding(List<String> urls) throws JsonProcessingException {
        ImageRequest imageRequest = getImageRequest(urls);
        return analyzeHouseImages(imageRequest);
    }

    public MaterialResult analyzeMaterial(List<String> urls) throws JsonProcessingException {
        ImageRequest imageRequest = getImageRequest(urls);
        Map<String, Object> result = analyzeMaterialImages(imageRequest);

        String material = null;
        String usage = null;

        List<Map<String, Object>> choices = (List<Map<String, Object>>) result.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            if (message != null) {
                String content = (String) message.get("content");

                // content 값을 JSON으로 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonData = objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {
                });

                // 필요한 데이터 추출
                material = (String) jsonData.get("material");
                usage = (String) jsonData.get("usage");
            }
        }

        return new MaterialResult(material, usage);
    }

    public BuildingUpdate analyzeHouseImages(ImageRequest imageRequest) throws JsonProcessingException {

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
        Map<String, Object> jsonData = objectMapper.readValue(jsonContent, new TypeReference<Map<String, Object>>() {
        });

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
        return buildingUpdate;
    }

    public Map<String, Object> analyzeMaterialImages(ImageRequest imageRequest) throws JsonProcessingException {

        // 시스템 메시지
        String systemMessage = """
                    너는 사진을 받으면 해당 사진에 나오는 부자재의 종류들과 그 사용법들을 반환하는 챗봇 AI다.
                
                    제공된 사진을 분석하여 부자재의 종류와 사용법을 양식에 맞게 작성된 JSON 형식으로 출력해야 한다.
                
                    반드시 양식에 맞게 작성해야 하며, 다른 방식으로의 응답은 절대 허용하지 않는다.
                
                    mateial과 usage는 여러개가 올 수 있지만 리스트 형식으로는 '절대' 출력하면 안된다.
                
                    mateiral과 usage가 여러 가지 이상으로 판단될 시, 반드시 ,로만 구분하여 한 개의 string으로 출력해야 한다.
                
                    예시 JSON은 다음과 같다.
                
                    {
                        "material" : "나무 판자, 쇠 파이프",
                        "usage" : "벽 만들기, 천장 고치기"
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
        String jsonContent = content.replaceAll("(?s)```json\\s*|```", "").trim();

        // JSON 데이터를 Map으로 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonData = objectMapper.readValue(jsonContent, new TypeReference<Map<String, Object>>() {
        });
        // 필요한 데이터 사용
        MaterialInfo materialInfo = new MaterialInfo((String) jsonData.get("material"), (String) jsonData.get("usage"));
        System.out.println(materialInfo);

        // 리턴값 반환
        return responseBody;
    }

    private static ImageRequest getImageRequest(List<String> urls) {
        ImageRequest imageRequest = new ImageRequest();
        List<ImageRequest.Content> contentList = new ArrayList<>();

        imageRequest.setRole("user");

        for (String url : urls) {
            ImageRequest.Content content = new ImageRequest.Content();
            ImageRequest.ImageUrl imageUrl = new ImageRequest.ImageUrl();

            imageUrl.setUrl(url);
            content.setType("image_url");
            content.setImage_url(imageUrl);
            contentList.add(content);
        }

        imageRequest.setContent(contentList);
        return imageRequest;
    }

}
