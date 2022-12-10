package com.lyh.shop.controller;

import com.lyh.shop.dto.OrderDto;
import com.lyh.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> order (@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors)
                sb.append(fieldError.getDefaultMessage());
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        String orderId;
        try {
            orderId = orderService.order(orderDto, email).toString();
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(orderId, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        if (!orderService.validateOrder(orderId, principal.getName()))
            return new ResponseEntity<String>("취소 권한이 없습니다", HttpStatus.FORBIDDEN);

        orderService.cancelOrder(orderId);
        return new ResponseEntity<String>(orderId.toString(), HttpStatus.OK);
    }

}
