package com.lyh.shop.repository;

import com.lyh.shop.dto.ItemSearchDto;
import com.lyh.shop.dto.MainItemDto;
import com.lyh.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
