package com.kittop.controller;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.dto.CartDTO;
import com.kittop.dto.ItemDTO;
import com.kittop.service.CartService;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kittop/addCart")
@RequiredArgsConstructor
@Log4j2
public class CartAddController {

    private final CartService cartService;
    private final HttpSession session;

    @GetMapping("/{itemId}")
    public Map<String, String> saveCart(@PathVariable("itemId") long itemId, @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        Map<String, String> map = new HashMap<>();
        String userEmail = userDetails.getUsername();
        log.info("SaveCart..." + userEmail);
        List<CartDTO> cartDTOS = cartService.findCartByUserEmail(userEmail);
        for(CartDTO cart : cartDTOS){
            if(cart.getItemId() == itemId){
                map.put("msg", "장바구니에 있는 상품입니다");
                return map;
            }
        }
        cartService.saveCart(itemId);
        map.put("msg", "장바구니에 추가되었습니다");
        map.put("cartCount", String.valueOf(cartService.findCartCountByUserEmail(userEmail)));

        return map;
    }

    @DeleteMapping("/{cartId}")
    public Map<String, Long> removeCart(@PathVariable("cartId") long cartId){
        Map<String, Long> map = new HashMap<>();
        cartService.deleteCart(cartId);

        map.put("cartId", cartId);

        return map;
    }
}
