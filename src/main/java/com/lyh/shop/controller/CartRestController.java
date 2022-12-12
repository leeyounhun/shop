package com.lyh.shop.controller;

import com.lyh.shop.dto.CartDetailDto;
import com.lyh.shop.dto.CartItemDto;
import com.lyh.shop.dto.ItemFormDto;
import com.lyh.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartRestController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors)
                sb.append(fieldError.getDefaultMessage());
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        String cartItemId;
        try {
            cartItemId = cartService.addCart(cartItemDto, email).toString();
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(cartItemId, HttpStatus.OK);
    }

    @GetMapping
    public ModelAndView orderHist(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        modelAndView.addObject("cartItems", cartDetailList);
        modelAndView.setViewName("cart/cartList");
        return modelAndView;
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<String> updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){

        if(count <= 0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        } else if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<String>(cartItemId.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/{cartItemId}")
    public @ResponseBody ResponseEntity<String> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){
        if (!cartService.validateCartItem(cartItemId, principal.getName()))
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<String>(cartItemId.toString(), HttpStatus.OK);
    }


}
