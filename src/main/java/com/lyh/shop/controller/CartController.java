package com.lyh.shop.controller;

import com.lyh.shop.dto.CartOrderDto;
import com.lyh.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping(value = "/orders")
    public @ResponseBody ResponseEntity<String> orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0)
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);

        for (CartOrderDto cartOrder : cartOrderDtoList)
            if(cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName()))
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);

        String orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName()).toString();
        return new ResponseEntity<String>(orderId, HttpStatus.OK);
    }
}
