package com.kittop.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PayCart {
    private ItemDTO itemDTO;
    private int count;
    private List<ItemDTO> cartList = new ArrayList<>();

    public void addCart(ItemDTO itemDTO){
        for(ItemDTO cart : cartList){
            if(cart.getItemId().equals(itemDTO.getItemId())){
                cart.setCount(itemDTO.getCount());
                return;
            }
        }
        cartList.add(itemDTO);
    }
}
