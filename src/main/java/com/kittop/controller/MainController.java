package com.kittop.controller;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.domain.Role;
import com.kittop.dto.ItemDTO;
import com.kittop.dto.PageRequestDTO;
import com.kittop.dto.PageResponseDTO;
import com.kittop.dto.UserDTO;
import com.kittop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    private final ItemService itemService;


    @GetMapping({"/", "/kittop"})
    public String mainPage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                           UserDTO userDTO,
                           Authentication authentication,
                           @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                           Model model) {
        /**
         * 전체 상품 목록 및 페이징 처리 확인
         */
        PageResponseDTO<ItemDTO> pageResponseDTO = itemService.findItemList(pageRequestDTO);
        log.info("keyword : " + pageRequestDTO.getKeyword());
        log.info("category: " + pageRequestDTO.getCategory());
        log.info("pageResponseDTO : " + pageResponseDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        // 검색 결과 목록만 따로 가져와서 모델에 추가
        List<ItemDTO> itemDTOList = pageResponseDTO.getDtoList();
        model.addAttribute("itemDTOList", itemDTOList);

        /**
         * 스프링 시큐리티 적용 된 로그인 정상여부
         * 로그 출력 확인
         */
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("로그인 성공");
            log.info("로그인 유저: " + authentication.getName());
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            log.info("유저 권한: " + authorities);

            if (authorities.equals(String.valueOf(Role.ROLE_GOOGLE))) {
                log.info("구글 계정 사용자는 최초 회원정보 추가 수정 필요합니다.");
                model.addAttribute("googleUserMsg",
                        "구글 계정으로 회원가입이 되었습니다. 추가 정보 수정 페이지로 이동합니다.");
                if (customUserDetails.getName().equals(authentication.getName())) {
                    model.addAttribute("userId", customUserDetails.getUserId());
                    log.info("userId : " + customUserDetails.getUserId());
                }
            }
        }
        else {
            log.info("로그아웃 상태입니다.");
        }
        return "main";
    }


}
