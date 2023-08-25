package com.kittop.controller;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.domain.Role;
import com.kittop.dto.UserDTO;
import com.kittop.service.EmailService;
import com.kittop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
@RequestMapping("/kittop")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    private final HttpSession session;

    /* 회원가입 페이지 */
    @GetMapping("/register")
    public String registerUserPage(Model model) {
        session.removeAttribute("emailConfirmationCode");
        model.addAttribute("userDTO", new UserDTO());
        return "user/registerUser";
    }



    /* 이메일 중복 체크(Ajax) */
    @GetMapping("/checkEmail")
    public ResponseEntity<Map<String, Boolean>>
    checkEmail(String email) {
        log.info("checkEmail ...");
        //이메일 중복 체크
        boolean exists = userService.isEmailExists(email);
        log.info("이메일 중복확인:" + exists);

        //JSON 형태(key, value)
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    /* 닉네임 중복 체크(Ajax) */
    @GetMapping("/checkNickName")
    public ResponseEntity<Map<String, Boolean>>
    checkNickName(@RequestParam("nickName") String nickName) {
        log.info("checkNickName ...");
        //닉네임 중복 체크
        boolean existNicks = userService.isNickNameExists(nickName);
        log.info("닉네임 중복확인:" + existNicks);

        //JSON 형태(key, value)
        Map<String, Boolean> nickResponse = new HashMap<>();
        nickResponse.put("existNicks", existNicks);
        return ResponseEntity.ok(nickResponse);
    }

    /* 회원가입 처리 */
    @PostMapping("/register")
    public String registerUserProcess(@Valid UserDTO userDTO,
                                      BindingResult bindingResult, Model model) {
        log.info("registerUserProcess ...");

        //UserDTO의 필드에 지정한 유효성 검증 메시지 가져와서 모델에 담기
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
                log.info("유효성 검증: " + error.getField() + ", Error: " + error.getDefaultMessage());
            }
            return "user/registerUser";
        }
        else {
            try {
                userService.saveUser(userDTO);
                log.info("userDTO :" + userDTO);
                model.addAttribute("userDTO", userDTO);
            } catch (IllegalStateException e) {
                model.addAttribute("errorMsg", e.getMessage());
                return "user/registerUser";
            }
        }
        return "redirect:/";
    }

    /* 로그인 페이지 */
    @GetMapping("/login")
    public String loginPage() {
        return "/login";

    }

    /*
     기존의 로그인 처리는 스프링 시큐리티를 적용함으로써
     중간에 시큐리티가 낚아채므로 더이상 동작되지 않음.
     */
//    @PostMapping("/kittop/login")
//    public String loginProcess(Model model, @RequestParam("email") String email) {
//        log.info("loginProcess ...");
//
//        try {
//            //입력 값이 없을 때
//            if (email == null || email.isEmpty()) {
//                model.addAttribute("errorMsg", "이메일을 입력해주세요.");
//                return "login";
//            }
//            UserDTO userDTO = userService.findUserByEmail(email);
//            log.info("조회된 userDTO: " + userDTO);
//            //일치하는 이메일이 없을 때
//            if (userDTO == null) {
//                model.addAttribute("errorMsg", "올바르지 않은 이메일입니다.");
//                return "login";
//            }
//        }
//        catch (Exception e) {
//            model.addAttribute("errorMsg", e.getMessage());
//            return "login";
//        }
//        log.info("로그인성공");
//        return "redirect:/";
//    }


    /* 회원 정보 페이지 */
    @GetMapping("/user/info")
    public String inquiryUserInfoPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      Model model) {
        UserDTO userDTO = userService.findUserByEmail(userDetails.getUsername());
        model.addAttribute("userDTO", userDTO);

        return "user/inquiryUser";
    }

    /* 회원 수정 페이지 */
    @GetMapping("/user/modify")
    public String modifyUserPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {

        UserDTO userDTO = userService.findUserByEmail(userDetails.getUsername());
        if(userDTO.getRole().equals(Role.ROLE_GOOGLE)){
            userDTO = UserDTO.builder()
                    .email(userDTO.getEmail())
                    .userId(userDTO.getUserId())
                    .password(encoder.encode(userDTO.getPassword()))
                    .role(Role.ROLE_GOOGLE_USER)
                    .build();
        }
        model.addAttribute("userDTO", userDTO);
        return "user/modifyUser";
    }

    /* 회원 수정 처리 */
    @PostMapping("/user/modify")
    public String modifyUserProcess(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @Valid UserDTO userDTO, BindingResult bindingResult,
                                    Model model) {
        log.info("modifyUserProcess ...");
        if (userService.findUserByUserId(userDTO.getUserId()) == null) {
            model.addAttribute("errorMsg", "not found user");
            return "redirect:/";
        }
        else {
            //UserDTO의 필드에 지정한 유효성 검증 메시지 가져와서 모델에 담기
            if (bindingResult.hasErrors() && !userDetails.getUserVO().getRole().equals(Role.ROLE_GOOGLE)
                    && !userDetails.getUserVO().getRole().equals(Role.ROLE_GOOGLE_USER)) {
                for (FieldError error : bindingResult.getFieldErrors()) {
                    model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
                    log.info("유효성 검증: " + error.getField() + ", Error: " + error.getDefaultMessage());
                }
                return "user/modifyUser";
            }
            else {
                try {
                    userService.updateUser(userDTO);
                    log.info("수정된 회원 정보 :" + userDTO);
                    model.addAttribute("userDTO", userDTO);
                }
                catch (IllegalStateException e) {
                    model.addAttribute("errorMsg", e.getMessage());
                    return "user/modifyUser";
                }
            }
        }
        if (userDTO.getRole().equals(Role.ROLE_GOOGLE) || userDTO.getRole().equals(Role.ROLE_GOOGLE_USER)) {
            return "redirect:/kittop/logout";
        }
        return "redirect:/kittop/user/info?userId=" + userDTO.getUserId();
    }

    /* 회원 삭제 처리 */
    @PostMapping("/user/delete")
    public String deleteUserProcess(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    HttpServletRequest request, HttpServletResponse response,
                                    Model model) {
        log.info("deleteUserProcess ...");

        if (userService.findUserByUserId(userDetails.getUserId()) == null) {
            model.addAttribute("errorMsg", "not found user");
            return "redirect:/";
        }
        else {
            userService.deleteUser(userDetails.getUserId());
            log.info("삭제가 완료 되었습니다");

            // 세션 만료 및 쿠키 삭제
            SecurityContextHolder.clearContext();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            for (Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        return "redirect:/";
    }


}

