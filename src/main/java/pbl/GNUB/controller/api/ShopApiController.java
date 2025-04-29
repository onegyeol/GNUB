package pbl.GNUB.controller.api;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import pbl.GNUB.dto.ShopDto;
import pbl.GNUB.entity.Member;
import pbl.GNUB.entity.Shop;
import pbl.GNUB.entity.ShopMenu;
import pbl.GNUB.repository.MemberRepository;
import pbl.GNUB.service.BookmarkService;
import pbl.GNUB.service.ShopService;

@RestController
@RequestMapping("/api/shop")
public class ShopApiController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookmarkService bookmarkService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getShopDetails(@PathVariable Long id, Principal principal) {
        Shop shop = shopService.findShopById(id);
        if (shop == null) {
            return ResponseEntity.notFound().build();
        }

        List<ShopMenu> menus = shopService.getMenusByShopName(shop.getName());

        boolean isBookmarked = false;
        boolean isLoggedIn = false;

        if (principal != null) {
            isLoggedIn = true; 
            String email = principal.getName();
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없음"));
            isBookmarked = bookmarkService.isBookmarked(member, shop);
        }

        Map<String, Object> response = Map.of(
                "shop", shop,
                "menus", menus,
                "isBookmarked", isBookmarked,
                "isLoggedIn", isLoggedIn 
        );

        return ResponseEntity.ok(response);
    }

}