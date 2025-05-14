package com.luizalabs.api.txt.purchase.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Purchase {

    @JsonProperty("user_id")
    private final int userId;

    @JsonProperty("name")
    private final String userName;

    private final Map<Integer, Order> ordersById = new HashMap<>();

    public Purchase(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Set<Order> getOrders() {
        return new HashSet<>(ordersById.values());
    }

    public Order getOrder(int orderId) {
        return ordersById.get(orderId);
    }

    public void addOrder(Order order) {
        ordersById.put(order.getId(), order);
    }

    public boolean containsOrder(int orderId) {
        return ordersById.containsKey(orderId);
    }
}
