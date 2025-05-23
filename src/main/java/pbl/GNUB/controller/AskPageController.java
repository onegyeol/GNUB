package pbl.GNUB.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AskPageController {

    @GetMapping("/ask")
    public String askPage() {
        return "form/recommend";
    }
}
