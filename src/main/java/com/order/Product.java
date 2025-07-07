package com.order;

import java.math.BigDecimal;

public class Product {
    private String name;
    private BigDecimal unitPrice;
    private String category;

    public Product(String name, BigDecimal unitPrice, String category) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.category = category;
    }
    
    public Product(String name, BigDecimal unitPrice) {
        this(name, unitPrice, null);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getCategory() {
        return category;
    }
} 