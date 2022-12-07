package com.lyh.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long id;

    private String itemName;

    private String itemDetail;

    private String imgUrl;

    private Integer itemPrice;

    @QueryProjection
    public MainItemDto(Long id, String itemName, String itemDetail, String imgUrl, Integer itemPrice) {
        this.id = id;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.itemPrice = itemPrice;
    }
}
