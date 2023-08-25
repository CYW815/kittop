package com.kittop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page = 1;

    @Builder.Default
    @Min(value = 10)
    @Max(value = 100)
    private int size = 10;

    private String link;
    private String[] types;
    private String keyword;
    private boolean finished;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;
    private String userEmail;
    private String category;
    private String status;

    public int getStart() {
        return (page - 1) * size;
    }

    public String getLink(){
        StringBuilder builder = new StringBuilder();
        builder.append("page=" + this.page);
        builder.append("&size=" + this.size);

        if(this.finished){
            builder.append("&finished=on");
        }

        if(this.keyword != null){
            for(int i = 0; i < this.types.length; i++){
                builder.append("&types=" + types[i]);
            }
        }

        if(this.keyword != null){
            try{
                builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        if(this.category != null){
            try{
                builder.append("&category=" + URLEncoder.encode(category, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        if(this.from != null){
            builder.append("&from=" + from.toString());
        }

        if(this.to != null) {
            builder.append("&to=" + to.toString());
        }
        return builder.toString();
    }

    public boolean checkType(String type){
        if(this.types == null || this.types.length == 0) {
            return false;
        }
        return Arrays.asList(this.types).contains(type);
    }
}
