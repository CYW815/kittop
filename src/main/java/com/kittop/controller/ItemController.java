package com.kittop.controller;

import com.kittop.dto.ItemDTO;
import com.kittop.dto.ReviewDTO;
import com.kittop.service.ItemService;
import com.kittop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@Log4j2
@RequestMapping("/kittop/*")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ReviewService reviewService;

    @GetMapping("item/info")
    public String inquiryItemInfoPage(Long itemId,
                                      ReviewDTO reviewDTO,
                                      Model model){
        ItemDTO itemDTO = itemService.findItemByItemId(itemId);

        itemService.updateItemHitByItemId(itemId);
        itemDTO = itemService.findItemByItemId(itemId); // 조회수를 업데이트한 후 다시 가져오기
        log.info("조회수: " + itemDTO.getHit());
        List<ReviewDTO> reviewDTOList = reviewService.findReviewInfoByItemId(itemId);
        model.addAttribute("reviewDTOList", reviewDTOList);
        model.addAttribute("item", itemDTO);
        log.info("제품 상세 정보 조회: " + itemDTO);
        return "/item/info";
    }



}
