package com.kittop.dto;

import com.kittop.domain.ItemVO;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewDTO {
    private long reviewId;
    private String title;
    private String content;
    private String userEmail;
    private Date regDate;
    private long itemId;
    private Long orderId;
    private ItemVO item;
}
