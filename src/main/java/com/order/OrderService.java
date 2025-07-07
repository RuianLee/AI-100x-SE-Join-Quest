package com.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private ThresholdDiscountPromotion thresholdDiscountPromotion;
    private BuyOneGetOnePromotion buyOneGetOnePromotion;
    
    public void setThresholdDiscountPromotion(ThresholdDiscountPromotion promotion) {
        this.thresholdDiscountPromotion = promotion;
    }
    
    public void setBuyOneGetOnePromotion(BuyOneGetOnePromotion promotion) {
        this.buyOneGetOnePromotion = promotion;
    }
    
    public Order checkout(List<OrderItem> items) {
        // Apply buy-one-get-one promotion first (affects final quantities)
        List<OrderItem> finalItems = applyBuyOneGetOnePromotion(items);
        
        Order order = new Order(finalItems);
        BigDecimal originalAmount = calculateTotalAmount(items); // Calculate based on original quantities
        order.setOriginalAmount(originalAmount);
        
        BigDecimal discount = calculateTotalDiscount(originalAmount);
        order.setDiscount(discount);
        order.setTotalAmount(originalAmount.subtract(discount));
        
        return order;
    }
    
    private List<OrderItem> applyBuyOneGetOnePromotion(List<OrderItem> originalItems) {
        List<OrderItem> finalItems = new ArrayList<>();
        
        for (OrderItem item : originalItems) {
            int finalQuantity = calculateFinalQuantityWithPromotion(item);
            finalItems.add(new OrderItem(item.getProduct(), finalQuantity));
        }
        
        return finalItems;
    }
    
    private int calculateFinalQuantityWithPromotion(OrderItem item) {
        int originalQuantity = item.getQuantity();
        
        // Apply buy-one-get-one promotion if applicable
        if (isBuyOneGetOneApplicable(item.getProduct())) {
            int freeQuantity = calculateBuyOneGetOneFreeQuantity(originalQuantity);
            return originalQuantity + freeQuantity;
        }
        
        return originalQuantity;
    }
    
    private int calculateBuyOneGetOneFreeQuantity(int purchasedQuantity) {
        // Buy-one-get-one logic: 
        // - Buy 1, get 1 free → total 2
        // - Buy 2, get 1 free → total 3 
        // - Buy 3, get 2 free → total 5
        // Formula: free = floor(quantity/2) + (quantity%2)
        return (purchasedQuantity / 2) + (purchasedQuantity % 2);
    }
    
    private boolean isBuyOneGetOneApplicable(Product product) {
        return buyOneGetOnePromotion != null && 
               buyOneGetOnePromotion.isApplicable(product);
    }
    
    private BigDecimal calculateTotalDiscount(BigDecimal originalAmount) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        
        // Apply threshold discount if configured and applicable
        if (thresholdDiscountPromotion != null && 
            thresholdDiscountPromotion.isApplicable(originalAmount)) {
            totalDiscount = totalDiscount.add(thresholdDiscountPromotion.getDiscount());
        }
        
        return totalDiscount;
    }
    
    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            BigDecimal itemTotal = item.getProduct().getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }
        return total;
    }
} 