package pbl.GNUB.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pbl.GNUB.entity.Shop;
import pbl.GNUB.repository.ShopRepository;


@Controller
public class MapController {
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;
    
    @GetMapping("/map")
    public String showMapPage(Model model) {
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        return "form/map";
    }
    
    
}
