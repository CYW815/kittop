package com.kittop.domain;


import lombok.*;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RippleVO {
    private long rippleId;
    private String content;
    private Date regDate;
    private long boardId;
}
