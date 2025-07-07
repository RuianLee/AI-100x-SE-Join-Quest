package com.order;

public class BuyOneGetOnePromotion {
    private String targetCategory;

    public BuyOneGetOnePromotion(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    public boolean isApplicable(Product product) {
        return product.getCategory() != null && 
               product.getCategory().equals(targetCategory);
    }

    public String getTargetCategory() {
        return targetCategory;
    }
} 