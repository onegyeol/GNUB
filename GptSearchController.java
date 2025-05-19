package pbl.GNUB.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import pbl.GNUB.service.GptService;


@RestController
public class GptSearchController {

    private final RestTemplate restTemplate;

    public GptSearchController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestParam("query") String query) {
        String url = "http://127.0.0.1:5000/ask"; // Flask API URL
        Map<String, String> body = new HashMap<>();
        body.put("query", query);

        // Flask 서버로 요청 보내기
        ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);

        return response;
    }
}
