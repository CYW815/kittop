package com.kittop.domain;


import lombok.*;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderListVO {
    private long orderId;
    private long itemId;
    private int count;
    private String userEmail;
    private Date regDate;
    private String status;
    private int totalPrice;
    private String addr;
    private String request;
    private String orderer;
    private String receiver;
    private String ordererPhone;
    private String receiverPhone;
    private String payment;
    private String tossOrderId;
    private String tossMethod;
    private String tossBank;
    private long reviewId;
    private long boardId;
}
