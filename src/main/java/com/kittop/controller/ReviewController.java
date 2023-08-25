package com.kittop.controller;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.dto.OrderListDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.dto.ReviewDTO;
import com.kittop.mapper.ItemMapper;
import com.kittop.service.ItemService;
import com.kittop.service.OrderListService;
import com.kittop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/kittop/review/*")
public class ReviewController {

    private final ReviewService reviewService;
    private final OrderListService orderListService;
    private final ItemMapper itemMapper;


    @GetMapping("/register")
    public String registerReviewForm(Long orderId, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        log.info("GET review register");
        ReviewDTO reviewDTO = reviewService.findReviewByOrderId(orderId);

        if (reviewDTO != null) {
            return "redirect:/kittop/order/list";
        }
        OrderListDTO orderListDTO = orderListService.findOrderListByOrderId(orderId);
        if (orderListDTO != null && orderListDTO.getUserEmail().equals(userDetails.getName())) {
            orderListDTO.setItem(itemMapper.findItemByItemId(orderListDTO.getItemId()));
            model.addAttribute("orderList", orderListDTO);
            log.info("get review register..");
            return "review/register";
        }
        return "redirect:/kittop/order/list";
    }


    @PostMapping("/register")
    public String registerReviewProcess(@Valid ReviewDTO reviewDTO, BindingResult bindingResult, OrderListDTO orderListDTO,
                                        RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("Review Register");
        reviewDTO.setUserEmail(userDetails.getName());
        log.info("POST review register");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/kittop/review/register";
        }
        reviewDTO.setUserEmail(userDetails.getName());
        reviewService.register(reviewDTO);
        log.info(orderListDTO.getOrderId());

        ReviewDTO reviewDTO1 = reviewService.findReviewByOrderId(orderListDTO.getOrderId());
        log.info(orderListDTO);
        orderListDTO.setReviewId(reviewDTO1.getReviewId());
        log.info(orderListDTO);

        orderListService.updateOrderListByOrderId(orderListDTO);
        log.info(orderListDTO);


        return "redirect:/kittop/review/list";

    }

    @GetMapping("/list")
    public String inquiryReviewList(PageRequestDTO pageRequestDTO, Model model) {
        log.info("review list");
        PageResponseDTO responseDTO = reviewService.findReviewByPageRequestDTO(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "review/list";
    }

    @GetMapping("/info")
    public String inquiryReviewInfo(Long reviewId, PageRequestDTO pageRequestDTO, Model model, Principal principal) {
        String loggedInEmail = principal.getName();
        log.info("principal" + loggedInEmail);
        ReviewDTO reviewDTO = reviewService.readReview(reviewId);
        PageResponseDTO responseDTO = reviewService.findReviewByPageRequestDTO(pageRequestDTO);
        log.info(reviewDTO);
        model.addAttribute("loggedInEmail", loggedInEmail);
        model.addAttribute("dto", reviewDTO);
        model.addAttribute("responseDTO", responseDTO);

        return "review/info";
    }


    @PostMapping("/delete")
    public String deleteReviewProcess(Long reviewId, RedirectAttributes redirectAttributes, PageRequestDTO pageRequestDTO,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("delete review");
        log.info("reviewId : " + reviewId);
        log.info("reviewId : " + pageRequestDTO);

        reviewService.remove(reviewId);

        return "redirect:/kittop/review/list?" + pageRequestDTO.getLink();
    }


}
