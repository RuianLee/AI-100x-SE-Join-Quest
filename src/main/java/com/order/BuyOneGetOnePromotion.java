package com.order;

public class BuyOneGetOnePromotion {
    private String targetCategory;

    public BuyOneGetOnePromotion(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    public String getTargetCategory() {
        return targetCategory;
    }

    public boolean isApplicable(String category) {
        return targetCategory.equals(category);
    }
} 