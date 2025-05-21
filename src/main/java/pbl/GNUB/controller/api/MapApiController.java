package pbl.GNUB.controller.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pbl.GNUB.service.ShopService;

@RestController
@RequestMapping("/api")
public class MapApiController {


    // Google Maps API Key 제공 (React 등에서 필요 시)
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @GetMapping("/google-maps-key")
    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }
}
