package com.kittop.controller;

import com.kittop.dto.ItemDTO;
import com.kittop.dto.OrderListDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.mapper.ItemMapper;
import com.kittop.mapper.OrderListMapper;
import com.kittop.service.ItemService;
import com.kittop.service.OrderListService;
import com.kittop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Log4j2
@RequestMapping("/kittop/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ItemService itemService;
    private final UserService userService;
    private final OrderListService orderListService;
    private final OrderListMapper orderListMapper;
    private final ItemMapper itemMapper;

    @GetMapping
    public String adminMainPage(Authentication authentication,
                                HttpServletRequest request, Model model) {
        /* 전체 상품 목록 출력 확인*/
        List<ItemDTO> itemDTOList = itemService.findItemAll();
        model.addAttribute("itemDTOList", itemDTOList);

        //이미지 저장경로 추가
        String itemImgPath = "C:/upload/item";
        model.addAttribute("itemImgPath", itemImgPath);

        /* 스프링 시큐리티 적용 된 로그인 정상여부
         * 로그 출력 확인
         */
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("로그인 성공");
            log.info("로그인 유저: " + authentication.getName());
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            log.info("유저 권한: " + authorities);
        }
        else {
            log.info("로그아웃 상태입니다.");
        }
        return "admin/admin";
    }

    //회원 목록 가져오기
    @GetMapping("/userlist")
    public String inquiryUserList(PageRequestDTO pageRequestDTO , Model model) {
        log.info("pageRequestDTO: " + pageRequestDTO);
        log.info("---------user List--------------");
        PageResponseDTO responseDTO = userService.findUserList(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        log.info("userList start");

        return "admin/userlist";
    }

    @GetMapping("item/register")
    public String registerItemPage(ItemDTO itemDTO) {
        return "item/registerItem";
    }

    @PostMapping("item/register")
    public String registerItemProcess(ItemDTO itemDTO, MultipartFile file, Model model) {
        log.info("item/register...");
        try {
            itemService.saveItem(itemDTO, file);
        }
        catch (Exception e) {
            model.addAttribute("errorMsg", "상품 등록중 에러가 발생했습니다.");
            e.printStackTrace();
            return "item/registerItem";
        }
        return "redirect:/";
    }

    @GetMapping("item/list")
    public String itemList(Model model){
        List<ItemDTO> itemDTOList = itemService.findItemAll();
        model.addAttribute("itemList", itemDTOList);
        return "/item/list";
    }

    @GetMapping("item/modify")
    public String modifyItemPage(long itemId, Model model){
        log.info("modifyItem...");
        ItemDTO itemDTO = itemService.findItemByItemId(itemId);
        model.addAttribute("item", itemDTO);
        return "item/modify";
    }

    @PostMapping("item/modify")
    public String modifyItemProcess(ItemDTO itemDTO, MultipartFile file){
        try {
            itemService.updateItem(itemDTO, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/kittop/admin";
    }

    @PostMapping("item/delete")
    public String deleteItemProcess(Long[] itemId){
        log.info("-----removeItem--------");
        log.info("itemId:" +itemId);
//        for(long L: itemId)
//            log.info(L);

        itemService.deleteSelectedItems(itemId);
        return "redirect:/kittop/admin/item/list";

    }

    @GetMapping("/order/list")
    public String inquiryOrderList(Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO<OrderListDTO> pageResponseDTO = orderListService.findOrderListByPageRequestDTO(pageRequestDTO);
        for(OrderListDTO order : pageResponseDTO.getDtoList()){
            order.setItem(itemMapper.findItemByItemId(order.getItemId()));
        }

        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        return "admin/orderlist";
    }

    @GetMapping("/order/info")
    public String inquiryOrderListInfo(Model model, long orderId){
        OrderListDTO orderListDTO = orderListService.findOrderListByOrderId(orderId);
        log.info(orderListDTO);
        orderListDTO.setItem(itemMapper.findItemByItemId(orderListDTO.getItemId()));
        log.info(orderListDTO);
        model.addAttribute("orderListDTO", orderListDTO);
        return "admin/orderinfo";
    }

    @PostMapping("/order/update")
    public String updateOrderListProcess(OrderListDTO orderListDTO){
        log.info(orderListDTO.getOrderId());
        orderListService.updateOrderList(orderListDTO);
        log.info(orderListDTO);
        return "redirect:/kittop/admin/order/info?orderId=" + orderListDTO.getOrderId() ;
    }

    @GetMapping("/order/sales")
    public String inquiryOrderListByPeriod(Model model, PageRequestDTO pageRequestDTO){
        if(pageRequestDTO.getFrom() == null || pageRequestDTO.getTo() == null){
            LocalDate now = LocalDate.now();
            pageRequestDTO.setFrom(now.withDayOfMonth(1));
            pageRequestDTO.setTo(now.plusDays(1));
        }
        String[] strlist = {"" ,"주문취소", "교환완료", "환불완료", "구매확정"};
        String str = "";
        String str1 = "";
        for(String s : strlist){
            str = str + "a";
            str1 = str1 + "b";
            model.addAttribute(str, orderListMapper.statusCount(s));
            model.addAttribute(str1, orderListMapper.statusTotal(s));
        }
        pageRequestDTO.setTo(pageRequestDTO.getTo().plusDays(1));
        PageResponseDTO pageResponseDTO = orderListService.findOrderListByPeriod(pageRequestDTO);
        pageRequestDTO.setTo(pageRequestDTO.getTo().minusDays(1));
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/sales";
    }

}
