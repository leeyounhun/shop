package com.lyh.shop.controller;

import com.lyh.shop.dto.ItemFormDto;
import com.lyh.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/item")
public class ItemRestController {

    private final ItemService itemService;

    @PostMapping
    public ModelAndView itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult
            , @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("item/itemForm");
            return modelAndView;
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            modelAndView.addObject("errorMessage", "첫번째 상품 이미지는 필수입니다.");
            modelAndView.setViewName("item/itemForm");
            return modelAndView;
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            modelAndView.addObject("errorMessage", "상품 등록 중 에러 발생");
            modelAndView.setViewName("item/itemForm");
            return modelAndView;
        }

        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @PutMapping("/{itemId}")
    public ModelAndView itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult
            , @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("item/itemForm");
            return modelAndView;
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            modelAndView.addObject("errorMessage", "첫번째 상품 이미지는 필수입니다.");
            modelAndView.setViewName("item/itemForm");
            return modelAndView;
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            modelAndView.addObject("errorMessage", "상품 수정 중 에러 발생");
            modelAndView.setViewName("item/itemForm");
            return modelAndView;
        }

        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
