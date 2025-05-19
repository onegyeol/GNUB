package pbl.GNUB.controller.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.entity.*;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.service.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/foodDetails")
@RequiredArgsConstructor
public class FoodDetailsApiController {

    private final ShopService shopService;
    private final BookmarkService bookmarkService;
    private final MemberRepository memberRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodDetails(@PathVariable Long id, Principal principal) {
        Shop shop = shopService.findShopById(id);
        if (shop == null) return ResponseEntity.notFound().build();

        List<ShopMenu> shopMenus = shopService.getMenusByShopName(shop.getName());

        boolean isBookmarked = false;
        boolean isLoggedIn = false;

        if (principal != null) {
            Member member = memberRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("사용자 정보 없음"));
            isBookmarked = bookmarkService.isBookmarked(member, shop);
            isLoggedIn = true;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("shop", shop);               
        response.put("shopMenus", shopMenus);    
        response.put("isBookmarked", isBookmarked);
        response.put("isLoggedIn", isLoggedIn);

        return ResponseEntity.ok(response);
    }
}
