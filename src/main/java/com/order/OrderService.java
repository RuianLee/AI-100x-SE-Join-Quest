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
        // Calculate original amount (sum of all item prices * original quantities)
        BigDecimal originalAmount = calculateOriginalAmount(items);
        
        // Apply buy-one-get-one promotion (affects final quantities for delivery)
        List<OrderItem> finalItems = new ArrayList<>();
        for (OrderItem item : items) {
            int finalQuantity = item.getQuantity();
            if (buyOneGetOnePromotion != null && 
                buyOneGetOnePromotion.isApplicable(item.getProduct().getCategory())) {
                // Add 1 free item for buy-one-get-one promotion
                finalQuantity = item.getQuantity() + 1;
            }
            finalItems.add(new OrderItem(item.getProduct(), finalQuantity));
        }
        
        Order order = new Order(finalItems);
        order.setOriginalAmount(originalAmount);
        
        // Apply promotions
        BigDecimal discount = BigDecimal.ZERO;
        
        // Apply threshold discount if configured and applicable
        if (thresholdDiscountPromotion != null && 
            thresholdDiscountPromotion.isApplicable(originalAmount)) {
            discount = discount.add(thresholdDiscountPromotion.getDiscount());
        }
        
        order.setDiscount(discount);
        order.setTotalAmount(originalAmount.subtract(discount));
        
        return order;
    }

    
    private BigDecimal calculateOriginalAmount(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            BigDecimal itemTotal = item.getProduct().getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);
        }
        return total;
    }
} 