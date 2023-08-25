package com.kittop.controller;


import com.kittop.dto.ItemDTO;
import com.kittop.dto.PayCart;
import com.kittop.service.CartService;
import com.kittop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/kittop/payCart")
@RequiredArgsConstructor
@Log4j2
public class PayCartController {

    private final ItemService itemService;
    private final CartService cartService;
    private final HttpSession session;

    @GetMapping(value = "/list")
    public PayCart cartList() {
        if(session.getAttribute("cart") != null){
            PayCart payCart = (PayCart) session.getAttribute("cart");
            log.info("CartList : " + payCart);
            return payCart;
        }

       return null;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PayCart cartAdd(@RequestBody ItemDTO itemDTO){
        int count = itemDTO.getCount();
        PayCart payCart = null;
        itemDTO = itemService.findItemByItemId(itemDTO.getItemId());
        itemDTO.setCount(count);
        if(session.getAttribute("cart") == null){
             payCart = new PayCart();
        }else{
            payCart = (PayCart)session.getAttribute("cart");
        }
        payCart.addCart(itemDTO);
        session.setAttribute("cart", payCart);
        return payCart;
    }

    @DeleteMapping("/{rno}")
    public PayCart delCart(@PathVariable("rno") long rno) {
        PayCart payCart = (PayCart) session.getAttribute("cart");
        List<ItemDTO> items = payCart.getCartList();
        log.info(items);
        for(ItemDTO item : items) {
            if (item.getItemId().equals(rno)){
                items.remove(item);
                break;
            }
        }
        return payCart;
    }

}


