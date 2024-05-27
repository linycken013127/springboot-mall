package net.ken.springbootmall.dto;

import jakarta.validation.constraints.Min;

public class BuyItem {

    private Integer productId;
    @Min(1)
    private Integer quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
