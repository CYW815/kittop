package com.kittop.controller;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.dto.OrderListDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.dto.PayCart;
import com.kittop.mapper.ItemMapper;
import com.kittop.service.OrderListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/kittop/order/*")
public class OrderController {

    private final OrderListService orderListService;
    private final ItemMapper itemMapper;
    private final HttpSession session;

    @GetMapping("/list")
    public String inquiryOrderList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model,
                                   PageRequestDTO pageRequestDTO){
        pageRequestDTO.setUserEmail(userDetails.getUsername());
        PageResponseDTO<OrderListDTO> pageResponseDTO = orderListService.findOrderListByUserEmailPageRequestDTO(pageRequestDTO);
        for(OrderListDTO order : pageResponseDTO.getDtoList()){
            order.setItem(itemMapper.findItemByItemId(order.getItemId()));
        }

        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        return "order/list";
    }

    @GetMapping("/info")
    public String inquiryOrderListInfo(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, long orderId,
                                       PageRequestDTO pageRequestDTO) {
        OrderListDTO orderListDTO = orderListService.findOrderListByOrderId(orderId);
        orderListDTO.setItem(itemMapper.findItemByItemId(orderListDTO.getItemId()));
        if(!orderListDTO.getUserEmail().equals(userDetails.getName())){
            return "redirect:/order/list";
        }
        model.addAttribute("orderListDTO", orderListDTO);
        return "order/info";
    }


    @GetMapping("/pay")
    public String payGet(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        if(session.getAttribute("cart") == null || ((PayCart)session.getAttribute("cart")).getCartList().isEmpty()){
            return "redirect:/kittop/cart/list";
        }
        model.addAttribute("user", userDetails.getUserVO());
        return "order/pay";
    }

    @PostMapping("/delete")
    public String deleteOrderListProcess(long orderId){
        orderListService.deleteOrderListByOrderId(orderId);
        return "redirect:/kittop/order/list";
    }

    @PostMapping("/update")
    public String updateOrderListProcess(OrderListDTO orderListDTO){
        orderListService.updateOrderList(orderListDTO);
        return "redirect:/kittop/order/list";
    }


}
