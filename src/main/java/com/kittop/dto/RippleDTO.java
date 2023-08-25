package com.kittop.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RippleDTO {
    private long rippleId;
    private String content;
    private Date regDate;
    private long boardId;
}
