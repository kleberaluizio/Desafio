package com.luizalabs.api.txt.purchase.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OrderTest {

    @Test
    @DisplayName("Should update order total value when others products are added")
    void ShouldUpdateTotalValue_WhenOthersProductsAreAdded() {
        Order order = new Order(1, "1948-10-09");
        Product product1 = new Product(1, BigDecimal.valueOf(100.23));
        Product product2 = new Product(2, BigDecimal.valueOf(37.55));
        Product product3 = new Product(2, BigDecimal.valueOf(21.10));
        BigDecimal expectedTotal = BigDecimal.valueOf(100.23 + 37.55 + 21.10);

        order.addProduct(product1);
        order.addProduct(product2);
        order.addProduct(product3);

        Assertions.assertEquals(expectedTotal, order.getTotal());
    }
}