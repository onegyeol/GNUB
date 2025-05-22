package pbl.GNUB.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class GptSearchController {

    private final RestTemplate restTemplate;

    public GptSearchController() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters()
            .add(new MappingJackson2HttpMessageConverter());
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askQuestion(@RequestBody Map<String, String> body) throws JsonProcessingException {
        String query = body.get("query");

        String url = "http://localhost:5000/chat";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> flaskResponse = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            Map.class
        );

        Map<String, String> result = new HashMap<>();
        if (flaskResponse.getStatusCode().is2xxSuccessful() && flaskResponse.getBody() != null) {
            Object replyObj = flaskResponse.getBody().get("reply");
            if (replyObj instanceof String reply) {
                result.put("reply", reply);  // ✅ key를 "reply"로 변경
            } else {
                result.put("reply", "응답 형식이 잘못되었습니다.");
            }
        } else {
            result.put("reply", "Flask 서버로부터 응답을 받지 못했습니다.");
        }
        return ResponseEntity.ok(result);

    }

}
