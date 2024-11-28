package pbl.GNUB.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pbl.GNUB.service.GptService;


@RestController
@RequestMapping("/api/gpt")
public class GptSearchController {

    private final GptService gptService;


    public GptSearchController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping("/ask")
    public String askGpt(@RequestParam String question) {
        return gptService.getGptResponse(question);
    }
}
