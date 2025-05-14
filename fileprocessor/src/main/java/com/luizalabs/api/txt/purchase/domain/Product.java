package com.luizalabs.api.txt.purchase.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record Product(
        @JsonProperty("product_id") int id,
        BigDecimal value
) {}
