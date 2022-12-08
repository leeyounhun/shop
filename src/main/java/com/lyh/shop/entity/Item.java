package com.lyh.shop.entity;

import com.lyh.shop.constant.ItemSellStatus;
import com.lyh.shop.dto.ItemFormDto;
import com.lyh.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.itemPrice = itemFormDto.getItemPrice();
        this.itemStock = itemFormDto.getItemStock();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int itemStock){
        int restStock = this.itemStock - itemStock;
        if (restStock < 0)
            throw new OutOfStockException("상품의 제고가 부족합니다.");
        this.itemStock = restStock;
    }

}
