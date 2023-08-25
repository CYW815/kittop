package com.kittop.controller;


import com.kittop.dto.ItemDTO;
import com.kittop.dto.OrderListDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.service.ItemService;
import com.kittop.service.OrderListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/kittop/loadPage")
@RequiredArgsConstructor
public class PageLoadController {

    private final ItemService itemService;
    private final OrderListService orderListService;


    @GetMapping("/{num}")
    public List<ItemDTO> pageLoad(@PathVariable("num") int num){
        log.info("loadPage : " + num);
        PageResponseDTO responseDTO = itemService.findItemList(PageRequestDTO.builder().page(num).size(8).build());
        log.info("loadPage" + responseDTO.getDtoList());
        return responseDTO.getDtoList();
    }

    @GetMapping("admin/order/list/{num}")
    public List<OrderListDTO> orderPageLoad(@PathVariable("num") int num){
        log.info("loadPage : " + num);
        PageResponseDTO responseDTO = orderListService.findOrderListByUserEmailPageRequestDTO(PageRequestDTO.builder().
                page(num).size(10).build());
        log.info("loadPage" + responseDTO.getDtoList());
        return responseDTO.getDtoList();
    }
}
