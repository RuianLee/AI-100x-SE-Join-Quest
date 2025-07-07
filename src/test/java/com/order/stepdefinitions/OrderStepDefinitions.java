package com.order.stepdefinitions;

import com.order.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderStepDefinitions {
    private OrderService orderService;
    private Order order;
    private List<OrderItem> orderItems;
    
    public OrderStepDefinitions() {
        this.orderService = new OrderService();
        this.orderItems = new ArrayList<>();
    }

    @Given("no promotions are applied")
    public void no_promotions_are_applied() {
        // No special configuration needed for no promotions scenario
        this.orderItems = new ArrayList<>();
    }

    @Given("the threshold discount promotion is configured:")
    public void the_threshold_discount_promotion_is_configured(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> config = rows.get(0);
        
        BigDecimal threshold = new BigDecimal(config.get("threshold"));
        BigDecimal discount = new BigDecimal(config.get("discount"));
        
        ThresholdDiscountPromotion promotion = new ThresholdDiscountPromotion(threshold, discount);
        orderService.setThresholdDiscountPromotion(promotion);
        
        this.orderItems = new ArrayList<>();
    }

    @Given("the buy one get one promotion for cosmetics is active")
    public void the_buy_one_get_one_promotion_for_cosmetics_is_active() {
        BuyOneGetOnePromotion promotion = new BuyOneGetOnePromotion("cosmetics");
        orderService.setBuyOneGetOnePromotion(promotion);
        
        this.orderItems = new ArrayList<>();
    }

    @When("a customer places an order with:")
    public void a_customer_places_an_order_with(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            int quantity = Integer.parseInt(row.get("quantity"));
            BigDecimal unitPrice = new BigDecimal(row.get("unitPrice"));
            
            // Check if category is specified in the data table
            String category = row.get("category");
            if (category == null) {
                category = "default";
            }
            
            Product product = new Product(productName, unitPrice, category);
            OrderItem item = new OrderItem(product, quantity);
            this.orderItems.add(item);
        }
        
        // Call checkout service
        this.order = orderService.checkout(this.orderItems);
    }

    @Then("the order summary should be:")
    public void the_order_summary_should_be(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> expectedSummary = rows.get(0);
        
        // Check total amount (always present)
        BigDecimal expectedTotalAmount = new BigDecimal(expectedSummary.get("totalAmount"));
        Assertions.assertEquals(expectedTotalAmount, order.getTotalAmount(), 
            "Total amount should match expected value");
        
        // Check original amount if present
        String originalAmountStr = expectedSummary.get("originalAmount");
        if (originalAmountStr != null) {
            BigDecimal expectedOriginalAmount = new BigDecimal(originalAmountStr);
            Assertions.assertEquals(expectedOriginalAmount, order.getOriginalAmount(),
                "Original amount should match expected value");
        }
        
        // Check discount if present
        String discountStr = expectedSummary.get("discount");
        if (discountStr != null) {
            BigDecimal expectedDiscount = new BigDecimal(discountStr);
            Assertions.assertEquals(expectedDiscount, order.getDiscount(),
                "Discount should match expected value");
        }
    }

    @And("the customer should receive:")
    public void the_customer_should_receive(DataTable dataTable) {
        List<Map<String, String>> expectedItems = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> expectedItem : expectedItems) {
            String expectedProductName = expectedItem.get("productName");
            int expectedQuantity = Integer.parseInt(expectedItem.get("quantity"));
            
            // Find matching item in order
            boolean found = false;
            for (OrderItem actualItem : order.getItems()) {
                if (actualItem.getProduct().getName().equals(expectedProductName)) {
                    Assertions.assertEquals(expectedQuantity, actualItem.getQuantity(),
                        "Quantity for " + expectedProductName + " should match expected value");
                    found = true;
                    break;
                }
            }
            
            Assertions.assertTrue(found, "Product " + expectedProductName + " should be found in order");
        }
    }
} 