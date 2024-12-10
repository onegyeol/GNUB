package pbl.GNUB.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GptService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskServerUrl = "http://localhost:5000/ask";

    public String getGptResponse(String userQuery) {
        try {
            // Flask 서버로 요청 보내기
            Map<String, String> requestBody = Map.of("query", userQuery);
            String response = restTemplate.postForObject(flaskServerUrl, requestBody, String.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Flask 서버 호출 중 오류가 발생했습니다.";
        }
    }
}
