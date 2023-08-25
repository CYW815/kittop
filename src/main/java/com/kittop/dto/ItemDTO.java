package com.kittop.dto;

import com.kittop.domain.ReviewVO;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDTO {


    private Long itemId;
    @NotEmpty
    private String category;
    @NotEmpty
    private String itemName;
    @NotEmpty
    private int price;
    private int stock;
    private int hit;
    @NotEmpty
    private String content;
    private String imgName;
    private Date regDate;
    private Date updateDate;
    private int count;
    private ReviewVO review;

}
