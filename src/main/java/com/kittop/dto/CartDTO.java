package com.kittop.dto;

import com.kittop.domain.ItemVO;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartDTO{
    private long cartId;
    private String userEmail;
    private long itemId;
    private ItemVO item;
}
