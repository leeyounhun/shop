package com.lyh.shop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderDto {

    @NotNull(message = "상품 아이디는 필수입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 수량은 1개 입니다.")
    @Max(value = 99, message = "최대 수량은 99개 입니다.")
    private int count;
}
