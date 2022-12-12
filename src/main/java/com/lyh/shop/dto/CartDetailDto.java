package com.lyh.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {

    private Long cartItemId;
    private String itemName;
    private int itemPrice;
    private int count;
    private String imgUrl;

    public CartDetailDto(Long cartItemId, String itemName, int itemPrice, int count, String imgUrl){
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.imgUrl = imgUrl;
    }

}