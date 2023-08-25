package com.kittop.controller;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.dto.CartDTO;
import com.kittop.dto.PayCart;
import com.kittop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/kittop/cart/*")
public class CartController {

    private final CartService cartService;
    private final HttpSession session;

    @GetMapping("/list")
    public String inquiryCartListPage(Model model,
                                      @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("이메일 세션: " + customUserDetails.getName());

        List<CartDTO> cartDTO = cartService.findCartByUserEmail(customUserDetails.getUsername());
        model.addAttribute("cartList", cartDTO);

        PayCart payCart = (PayCart) session.getAttribute("cart");
        if (payCart != null) {
            model.addAttribute("cart", payCart);
        }
        else {
            payCart = new PayCart(); // 새로운 PayCart 객체 생성
        }
        session.setAttribute("cart", payCart); // 세션에 저장
        model.addAttribute("cart", payCart);

        return "cart/list";
    }

//    @GetMapping("/list")
//    public String inquiryCartListPage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails){
//
//        log.info("이메일 세션: " + customUserDetails.getName());
//
//        List<CartDTO> cartDTO = cartService.findCartByUserEmail(customUserDetails.getUsername());
//        model.addAttribute("cartList", cartDTO);
//
//        PayCart payCart = (PayCart) session.getAttribute("cart");
//        if (payCart == null) {
//            payCart = new PayCart();
//            session.setAttribute("cart", payCart);
//        }
//        model.addAttribute("cart", payCart);
//
//        return "cart/list";
//    }
//
//    // JSON response
//    @GetMapping("/list-json")
//    @ResponseBody
//    public ResponseEntity<List<CartDTO>> getCartList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
//        if(session.getAttribute("cart") != null){
//            session.removeAttribute("cart");
//        }
//        log.info("이메일 세션: " + customUserDetails.getName());
//
//        List<CartDTO> cartDTO = cartService.findCartByUserEmail(customUserDetails.getUsername());
//
//        PayCart payCart = (PayCart) session.getAttribute("cart");
//        if (payCart != null) {
//
//        } else {
//            payCart = new PayCart();
//            session.setAttribute("cart", payCart);
//        }
//
//        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
//    }

}
