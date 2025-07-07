package com.order;

import java.math.BigDecimal;

public class ThresholdDiscountPromotion {
    private BigDecimal threshold;
    private BigDecimal discount;

    public ThresholdDiscountPromotion(BigDecimal threshold, BigDecimal discount) {
        this.threshold = threshold;
        this.discount = discount;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public boolean isApplicable(BigDecimal originalAmount) {
        return originalAmount.compareTo(threshold) >= 0;
    }
} 