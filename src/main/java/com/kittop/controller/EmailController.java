package com.kittop.controller;

import com.kittop.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kittop")
@Log4j2
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/emailConfirm")
    public ResponseEntity<Map<String, String>> confirmEmail(@RequestParam("email") String email, HttpSession session) {
        try {
            // 이메일 인증 로직 수행
            String confirm = emailService.sendSimpleMessage(email);

            // 인증 코드를 세션에 저장
            session.setAttribute("emailConfirmationCode", confirm);
            log.info("emailConfirm 세션: " + confirm);

            // 인증 코드를 성공적으로 생성하고 이메일을 보냈다는 정보를 JSON 형태로 클라이언트에 반환
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "이메일이 성공적으로 발송되었습니다.");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("이메일 발송 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/emailVerify")
    public ResponseEntity<Map<String, Boolean>> verifyEmail(@RequestParam("code") String code, HttpSession session) {
        try {
            // 세션에서 인증 코드를 가져옴
            String emailConfirmationCode = (String) session.getAttribute("emailConfirmationCode");

            // 인증 코드가 일치하는지 검사
            boolean isCodeValid = (emailConfirmationCode != null && emailConfirmationCode.equals(code));

            // 결과를 JSON 형태로 클라이언트에 반환
            Map<String, Boolean> responseBody = new HashMap<>();
            responseBody.put("success", isCodeValid);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("이메일 인증 코드 검증 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/getServerCode")
    public String getServerCode(HttpSession session){
        return (String) session.getAttribute("emailConfirmationCode");
    }

}