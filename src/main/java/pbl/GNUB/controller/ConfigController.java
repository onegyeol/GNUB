package pbl.GNUB.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/config")
public class ConfigController {
    @Value("${openai.api.key}")
    private String openaiApiKey;

    @GetMapping("/apiKey")
    public ResponseEntity<String> getApiKey() {
        return ResponseEntity.ok(openaiApiKey);
    }
    @GetMapping("/database")
    public Map<String, String> getDatabaseInfo() {
        return Map.of("key1", "value1", "key2", "value2");
    }
}
 