package com.lyh.shop.repository;

import com.lyh.shop.constant.ItemSellStatus;
import com.lyh.shop.dto.ItemSearchDto;
import com.lyh.shop.entity.Item;
import com.lyh.shop.entity.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory jpaQueryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (StringUtils.equals("all", searchDateType) || searchDateType == null)
            return null;
        else if (StringUtils.equals("1d", searchDateType))
            localDateTime = localDateTime.minusDays(1);
        else if (StringUtils.equals("1w", searchDateType))
            localDateTime = localDateTime.minusWeeks(1);
        else if (StringUtils.equals("1m", searchDateType))
            localDateTime = localDateTime.minusMonths(1);
        else if (StringUtils.equals("6m", searchDateType))
            localDateTime = localDateTime.minusMonths(6);

        return QItem.item.regTime.after(localDateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("itemName", searchBy))
            return QItem.item.itemName.like("%" + searchQuery + "%");
        else if (StringUtils.equals("createBy", searchBy))
            return QItem.item.createdBy.like("%" + searchQuery + "%");

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> content = jpaQueryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType())
                        , searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
                        , searchSellStatusEq(itemSearchDto.getSearchSellStatus()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }
}
