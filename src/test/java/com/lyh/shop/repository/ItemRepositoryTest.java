package com.lyh.shop.repository;

import com.lyh.shop.constant.ItemSellStatus;
import com.lyh.shop.entity.Item;
import com.lyh.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(ItemRepositoryTest.class);
    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setItemPrice(10000);
        item.setItemDetail("상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setItemStock(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        log.info(savedItem.toString());
    }

    public void createItemList(){
        for (int i = 1; i < 6; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setItemPrice(10000 + i);
            item.setItemDetail("상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setItemStock(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
        for (int i = 6; i < 11; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setItemPrice(10000 + i);
            item.setItemDetail("상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setItemStock(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemName("테스트 상품 1");
        for (Item item : itemList)
            log.info(item.toString());
    }

    @Test
    @DisplayName("@Query 를 이용한 상품 조회 테스트")
    public  void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("상품 상세 설명");
        for (Item item : itemList)
            log.info(item.toString());
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createItemList();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = jpaQueryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "상품 상세 설명" + "%"))
                .orderBy(qItem.itemPrice.desc());
        List<Item> itemList = query.fetch();
        for (Item item : itemList)
            log.info(item.toString());
    }
    @Test
    @DisplayName("Querydsl 조회 테스트2")
    public void queryDslTest2(){
        this.createItemList();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "상품 상세 설명";
        int itemPrice = 10003;
        String itemSellStatus = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.itemPrice.gt(itemPrice));

        if (StringUtils.equals(itemSellStatus, ItemSellStatus.SELL))
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPage = itemRepository.findAll(booleanBuilder, pageable);
        log.info("total elements : " + itemPage.getTotalElements());

        List<Item> itemList = itemPage.getContent();
        for (Item item1 : itemList)
            log.info(item1.toString());
    }


}