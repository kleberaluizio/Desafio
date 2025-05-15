package com.luizalabs.api.txt.purchase.service.parameters;

import java.math.BigDecimal;

public class SanitizedEntryParams {
    public int userId;
    public String userName;
    public int orderId;
    public int productId;
    public BigDecimal productValue;
    public String date;

    public SanitizedEntryParams(String entry) {
        this.userId = Integer.parseInt(entry.substring(0, 10).trim());
        this.userName = entry.substring(10, 55).trim();
        this.orderId = Integer.parseInt(entry.substring(55, 65).trim());
        this.productId = Integer.parseInt(entry.substring(65, 75).trim());
        this.productValue = BigDecimal.valueOf(Double.parseDouble(entry.substring(75, 87).trim()));
        String rawDate = entry.substring(87);
        this.date = rawDate.substring(0, 4) + "-" + rawDate.substring(4, 6) + "-" + rawDate.substring(6);
    }
}
