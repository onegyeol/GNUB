package pbl.GNUB.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GptService {

    public String getGptResponse(String userQuery) {
        try {
            // 데이터베이스에서 관련 데이터 검색
            List<Shop> matchingShops = searchShops(userQuery);

            // GPT 프롬프트 생성
            String prompt = createPrompt(userQuery, matchingShops);

            // HTTP 요청 생성
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + gptApiKey);
            headers.set("Content-Type", "application/json");

            String requestBody = new ObjectMapper().writeValueAsString(Map.of(
                "model", "gpt-3.5-turbo", 
                "messages", List.of(Map.of("role", "user", "content", prompt))
            ));

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(
                gptApiUrl,
                HttpMethod.POST,
                request,
                String.class
            );

            // GPT 응답 처리
            Map<String, Object> responseBody = new ObjectMapper().readValue(response.getBody(), Map.class);
            String gptMessage = (String) ((Map<String, Object>) ((List<?>) responseBody.get("choices")).get(0))
                    .get("message");

            return gptMessage;

        } catch (Exception e) {
            e.printStackTrace();
            return "GPT 응답을 생성하는 중 오류가 발생했습니다.";
        }
    }

    // GPT에 전달할 프롬프트 생성
    private String createPrompt(String userQuery, List<Shop> shops) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("사용자가 ").append(userQuery).append("에 대해 질문했습니다.\n");
        prompt.append("다음은 관련된 음식점 데이터입니다:\n");

        for (Shop shop : shops) {
            prompt.append("- ").append(shop.getName())
                  .append(" (").append(shop.getMainMenu()).append("): ")
                  .append(shop.getAddress()).append("\n");
        }

        prompt.append("위 데이터를 기반으로 사용자의 질문에 대한 적절한 답변을 생성해주세요.");
        return prompt.toString();
    }

    public List<Shop> searchShops(String query) {
        // 태그 추출 및 데이터 검색 로직 그대로 사용
        List<String> tags = extractTagsFromQuery(query);
        List<Shop> shops = new ArrayList<>();

        for (String tag : tags) {
            shops.addAll(shopRepository.findByShopTags(tag));
        }
        shops.addAll(shopRepository.findByNameContaining(query));
        shops.addAll(shopRepository.findByMainMenuContaining(query));

        return shops.stream().distinct().collect(Collectors.toList());
    }

    private List<String> extractTagsFromQuery(String query) {
        List<String> tags = new ArrayList<>();
        List<String> allTags = Arrays.asList(
            "가성비", "맛있는", "신선한", "친절한", "혼밥", "재방문률이 높은", 
            "깔끔하고 분위기가 좋은", "위생", "최근에 자주가는", "가좌동", "칠암동"
        );

        for (String tag : allTags) {
            if (query.contains(tag)) {
                tags.add(tag);
            }
        }
    }
}
