package com.luizalabs.api.txt.purchase.domain;

import com.luizalabs.api.txt.purchase.util.DataFormatter;

import java.time.LocalDate;

public class OptionalFilters {

    private final Integer orderId;
    private LocalDate startDate;
    private LocalDate endDate;
    private final boolean hasDateFilter;

    public OptionalFilters(Integer orderId, String startDate, String endDate) {
        this.orderId = orderId;

        if (startDate == null && endDate == null) {
            this.hasDateFilter = false;
            return;
        }
        this.startDate = DataFormatter.format(startDate);
        this.endDate = DataFormatter.format(endDate);
        this.hasDateFilter = true;
    }

    public boolean isExcludedByOrderIdFilter(int orderId) {
        return this.orderId != null && !this.orderId.equals(orderId);
    }

    public boolean isExcludedByDateRangeFilter(String orderDate) {
        if (!this.hasDateFilter) {
            return false;
        }
        LocalDate date = DataFormatter.format(orderDate);
        boolean isBetweenStartAndEndDate = date.isAfter(this.startDate) && date.isBefore(this.endDate);
        boolean isInRange = (date.equals(this.startDate) || date.equals(this.endDate) || isBetweenStartAndEndDate);
        return !isInRange;
    }

}
