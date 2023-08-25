package com.kittop.domain;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartVO {
    private long cartId;
    private String userEmail;
    private long itemId;

}
