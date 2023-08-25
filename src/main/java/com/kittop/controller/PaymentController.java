package com.kittop.controller;

import com.kittop.config.auth.CustomUserDetails;
import com.kittop.dto.CartDTO;
import com.kittop.dto.ItemDTO;
import com.kittop.dto.OrderListDTO;
import com.kittop.dto.PayCart;
import com.kittop.service.CartService;
import com.kittop.service.OrderListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value="/kittop/toss/")
public class PaymentController {

    private final OrderListService orderListService;
    private final CartService cartService;
    private final HttpSession session;


    @PostMapping("payment")
    public String payment(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, OrderListDTO orderListDTO){
        model.addAttribute("user", userDetails);
        session.setAttribute("orderDTO", orderListDTO);
        PayCart payCart = (PayCart) session.getAttribute("cart");
        int amount = 0;
        for(ItemDTO c : payCart.getCartList()){
            amount += c.getPrice() * c.getCount();
        }
        model.addAttribute("amount", amount);
        return "toss/payment";
    }

    @GetMapping(value = "success")
    public String paymentResult(
            Model model,
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "amount") Integer amount,
            @RequestParam(value = "paymentKey") String paymentKey,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        String secretKey = "test_ak_ZORzdMaqN3wQd5k6ygr5AkYXQGwy:";

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;
        model.addAttribute("isSuccess", isSuccess);

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        model.addAttribute("responseStr", jsonObject.toJSONString());
        System.out.println(jsonObject.toJSONString());

        model.addAttribute("method", (String) jsonObject.get("method"));
        model.addAttribute("orderName", (String) jsonObject.get("orderName"));
        model.addAttribute("totalPrice", String.valueOf(jsonObject.get("totalAmount")));
        model.addAttribute("customerName", (String) ((JSONObject)jsonObject.get("virtualAccount")).get("customerName"));

        if (((String) jsonObject.get("method")) != null) {
            if(session.getAttribute("orderDTO") != null){
                OrderListDTO orderListDTO = (OrderListDTO) session.getAttribute("orderDTO");
                orderListDTO.setOrderer(userDetails.getUserVO().getName());
                orderListDTO.setOrdererPhone(userDetails.getUserVO().getPhone());
                orderListDTO.setUserEmail(userDetails.getUsername());
                if (((String) jsonObject.get("method")).equals("가상계좌")){
                    orderListDTO.setStatus("결제전");
                }
                else{
                    orderListDTO.setStatus("결제완료");
                }
                orderListDTO.setTossOrderId((String) jsonObject.get("orderId"));
                orderListDTO.setTossMethod((String) jsonObject.get("method"));
                orderListDTO.setTossBank((String) ((JSONObject) jsonObject.get("virtualAccount")).get("bank"));
                log.info(orderListDTO);
                PayCart payCart = (PayCart) session.getAttribute("cart");

                String email = userDetails.getName();
                log.info("payment email : " + email);

                for(ItemDTO item : payCart.getCartList()){
                    orderListDTO.setItemId(item.getItemId());
                    long itemId = orderListDTO.getItemId();
                    log.info("payment itemId : " + itemId);
                    orderListDTO.setCount(item.getCount());
                    orderListDTO.setTotalPrice(item.getCount() * item.getPrice());
                    orderListService.saveOrder(orderListDTO);
                    cartService.deleteCartByUserEmail(email, item.getItemId());
                }

            }
            if (((String) jsonObject.get("method")).equals("카드")) {
                model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
            } else if (((String) jsonObject.get("method")).equals("가상계좌")) {
                model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
            } else if (((String) jsonObject.get("method")).equals("계좌이체")) {
                model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
            } else if (((String) jsonObject.get("method")).equals("휴대폰")) {
                model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
            }
        } else {
            model.addAttribute("code", (String) jsonObject.get("code"));
            model.addAttribute("message", (String) jsonObject.get("message"));
        }

        return "toss/success";
    }

    @GetMapping(value = "fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code
    ) throws Exception {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "toss/fail";
    }

}