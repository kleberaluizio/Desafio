package com.luizalabs.api.txt.purchase.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    @JsonProperty("order_id")
    private final int id;

    private BigDecimal total = BigDecimal.ZERO;

    private final String date;

    private final List<Product> products = new ArrayList<>();

    public Order(int id, String date) {
        this.id = id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public List<Product> getProducts() {
        return List.copyOf(products);
    }

    public void addProduct(Product product) {
        total = total.add(product.value());
        products.add(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }
}
