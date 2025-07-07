package com.order;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private BigDecimal totalAmount;
    private BigDecimal originalAmount;
    private BigDecimal discount;
    private List<OrderItem> items;

    public Order(List<OrderItem> items) {
        this.items = items;
        this.totalAmount = BigDecimal.ZERO;
        this.originalAmount = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public List<OrderItem> getItems() {
        return items;
    }
} 