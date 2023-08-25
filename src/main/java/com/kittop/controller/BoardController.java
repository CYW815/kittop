package com.kittop.controller;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.dto.BoardDTO;
import com.kittop.dto.OrderListDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.mapper.ItemMapper;
import com.kittop.service.BoardService;
import com.kittop.service.OrderListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/kittop/board/*")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final OrderListService orderListService;
    private final ItemMapper itemMapper;

    @GetMapping("/register")
    public String registerBoardPage(PageRequestDTO pageRequestDTO){
        log.info("get board register..");
        return "board/register";
    }

    @PostMapping("/register")
    public String registerBoardProcess(@Valid BoardDTO boardDTO, BindingResult bindingResult
            , RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        log.info("register start...");
        boardDTO.setUserEmail(userDetails.getName());

        log.info("category"+ boardDTO.getCategory());
        log.info(userDetails.getUsername());
        log.info("board POST register...");
        if(bindingResult.hasErrors()) {

            log.info("has errors...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/kittop/board/register";
        }
        boardDTO.setUserEmail(userDetails.getName());

        boardService.saveBoard(boardDTO);
        return "redirect:/kittop/board/list";

    }

    @GetMapping("/list")
    public String inquiryBoardList(PageRequestDTO pageRequestDTO, Model model){
        log.info("board list...");
        pageRequestDTO.setCategory("Q");
        PageResponseDTO responseDTO = boardService.list(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "board/list";
    }

    @GetMapping("/info")
    public String inquiryBoardInfo(Long boardId, PageRequestDTO pageRequestDTO, Model model,Principal principal){
        String loggedInEmail = principal.getName();
        log.info("principal......"+loggedInEmail);
        BoardDTO boardDTO = boardService.findBoardByBoardId(boardId);
        log.info(boardDTO);
        model.addAttribute("loggedInEmail", loggedInEmail);
        model.addAttribute("dto", boardDTO);
        return "board/info";
    }

    @GetMapping("/modify")
    public String inquiryBoardInfo2(Long boardId, PageRequestDTO pageRequestDTO, Model model, Principal principal){
        String loggedInEmail = principal.getName();

        log.info("-----modify-----------");
        BoardDTO boardDTO = boardService.findBoardByBoardId(boardId);
        log.info("modify..."+ boardDTO);
        log.info(boardDTO);

        if(boardId != null && boardDTO.getUserEmail().equals(loggedInEmail)){
            model.addAttribute("dto", boardDTO);
            return "board/modify";
        } else {
            return "redirect:/kittop/board/list";
        }
    }


    @PostMapping("/modify")
    public String modifyBoardProcess(PageRequestDTO pageRequestDTO, @Valid BoardDTO boardDTO,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails userDetails ) { //유효성검사결과 에러가 있으면 수정페이지로 돌아감

        log.info("----------modify--------------");
        boardDTO.setUserEmail(userDetails.getUsername());
        if (bindingResult.hasErrors()) {
            log.info("modify has error...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("boardId", boardDTO.getBoardId());//tno가 쿼리스트링(like '?xx=1')으로 전달
            return "redirect:/board/modify";
        }
        boardService.updateBoard(boardDTO);

        redirectAttributes.addAttribute("boardId", boardDTO.getBoardId());

        return "redirect:/kittop/board/info";

    }

    @PostMapping("/delete")
    public String deleteBoardProcess(Long boardId,RedirectAttributes redirectAttributes, PageRequestDTO pageRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails){

      log.info("------------remove-------------");
      log.info("boardId :" + boardId);
      log.info("boardId:" +pageRequestDTO);

      boardService.deleteBoardByBoardId(boardId);
        return "redirect:/kittop/board/list";

    }

    @GetMapping("/ex_register")
    public String exRegisterBoardPage(long orderId, @AuthenticationPrincipal CustomUserDetails userDetails,
                                      PageRequestDTO pageRequestDTO,
                                      Model model) {
        BoardDTO boardDTO = boardService.findBoardbyOrderId(orderId);
        if (boardDTO != null) {
            return "redirect:/kittop/order/list";
        }
        OrderListDTO orderListDTO = orderListService.findOrderListByOrderId(orderId);
        if (orderListDTO != null && orderListDTO.getUserEmail().equals(userDetails.getName())) {
            orderListDTO.setItem(itemMapper.findItemByItemId(orderListDTO.getItemId()));
            model.addAttribute("orderList", orderListDTO);
            log.info("get board register..");
            return "board/ex_register";
        }

        return "redirect:/kittop/order/list";
    }

    @PostMapping("/ex_register")
    public String exRegisterBoardProcess(@ModelAttribute("boardDTO") BoardDTO boardDTO, OrderListDTO orderListDTO, BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes, HttpSession session,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("register start...");
        boardDTO.setUserEmail(userDetails.getName());
        log.info(userDetails.getUsername());
        log.info("board POST register...");

        if (bindingResult.hasErrors()) {
            log.info("has errors...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/kittop/board/ex_register";
        }

        boardDTO.setUserEmail(userDetails.getName());
        boardService.saveReBoard(boardDTO);

        BoardDTO boardDTO1 = boardService.findBoardbyOrderId(orderListDTO.getOrderId());
        orderListDTO.setBoardId(boardDTO1.getBoardId());
        log.info(orderListDTO);
        if (boardDTO1.getCategory().equals("E")) {
            orderListDTO.setStatus("교환신청");
        } else if (boardDTO1.getCategory().equals("R")) {
            orderListDTO.setStatus("환불신청");
        }

        orderListService.updateOrderList(orderListDTO);
        return "redirect:/kittop/order/list";
    }

    @GetMapping("/ex_list")
    public String inquiryEx_BoardList(PageRequestDTO pageRequestDTO, Model model){
        log.info("board ex_list...");
        pageRequestDTO.setCategory("E");
        PageResponseDTO responseDTO = boardService.list(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "board/ex_list";
    }
    @GetMapping("/re_list")
    public String inquiryRe_BoardList(PageRequestDTO pageRequestDTO, Model model){
        log.info("board re_list...");
        pageRequestDTO.setCategory("R");
        PageResponseDTO responseDTO = boardService.list(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "board/re_list";
    }

}
