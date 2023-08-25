package com.kittop.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardVO {
    private long boardId;
    private String title;
    private String content;
    private String userEmail;
    private LocalDate regDate;
    private LocalDate updateDate;
    private String category;
    private long orderId;
}
