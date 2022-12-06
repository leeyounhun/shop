package com.lyh.shop.repository;

import com.lyh.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    List<Item> findByItemName(String itemName);



    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.itemPrice desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

}
