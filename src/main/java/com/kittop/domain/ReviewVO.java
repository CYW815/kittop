package com.kittop.domain;


import lombok.*;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewVO {
    private long reviewId;
    private String title;
    private String content;
    private String userEmail;
    private Date regDate;
    private long itemId;
    private Long orderId;
}
