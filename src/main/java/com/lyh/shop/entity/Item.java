package com.lyh.shop.entity;

import com.lyh.shop.constant.ItemSellStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Data
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemId;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(name = "item_price", nullable = false)
    private int itemPrice;

    @Column(nullable = false)
    private int itemStock;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
