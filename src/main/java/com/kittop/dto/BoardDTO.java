package com.kittop.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.security.Timestamp;
import java.time.LocalDate;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDTO {


    private Long boardId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private String userEmail;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate regDate;

    private LocalDate updateDate;


    private String category;
    private long orderId;


}
