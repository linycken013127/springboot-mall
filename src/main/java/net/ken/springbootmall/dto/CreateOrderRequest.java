package net.ken.springbootmall.dto;

import jakarta.validation.Valid;

import java.util.List;

public class CreateOrderRequest {

    @Valid
    private List<BuyItem> buyItemList;

    public List<BuyItem> getBuyItemList() {
        return buyItemList;
    }

    public void setBuyItemList(List<BuyItem> buyItemList) {
        this.buyItemList = buyItemList;
    }
}
