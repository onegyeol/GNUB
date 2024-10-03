package pbl.GNUB.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class MainController {

    @GetMapping("/main")
    public String homePage() {
        return "form/main";
    }
    
    @GetMapping("/search") // 검색결과 페이지
    public String searchPage() {
        return "form/search";
    }

    @GetMapping("/shopDetails") // 음식점상세 페이지
    public String foodDetailsPage() {
        return "form/foodDetails";
    }
    
    
}
