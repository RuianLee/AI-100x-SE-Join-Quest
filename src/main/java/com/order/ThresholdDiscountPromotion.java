package com.order;

import java.math.BigDecimal;

public class ThresholdDiscountPromotion {
    private BigDecimal threshold;
    private BigDecimal discount;

    public ThresholdDiscountPromotion(BigDecimal threshold, BigDecimal discount) {
        this.threshold = threshold;
        this.discount = discount;
    }

    public boolean isApplicable(BigDecimal amount) {
        return amount.compareTo(threshold) >= 0;
    }

    public BigDecimal getDiscount() {
        return discount;
    }
} 