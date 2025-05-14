package com.luizalabs.api.txt.purchase.service;

import com.luizalabs.api.txt.purchase.domain.OptionalFilters;
import com.luizalabs.api.txt.purchase.domain.Order;
import com.luizalabs.api.txt.purchase.domain.Product;
import com.luizalabs.api.txt.purchase.domain.Purchase;
import com.luizalabs.api.txt.purchase.util.DataFormatter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

@Service
public class FileProcessorServiceBean {

    public Collection<Purchase> processFile(MultipartFile file, OptionalFilters filters) {
        Map<Integer, Purchase> purchasesByUserId = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String rawEntry;
            while ((rawEntry = reader.readLine()) != null) {
                SanitizedEntry entry = new SanitizedEntry(rawEntry);

                if (filters.isExcludedByOrderIdFilter(entry.orderId)) {
                    continue;
                }
                if (filters.isExcludedByDateRangeFilter(entry.date)) {
                    continue;
                }

                addEntryToPurchase(entry, purchasesByUserId);
                System.out.println(entry.userId + " " + entry.userName + " " + entry.orderId + " " + entry.productId + " " + entry.productValue + " " + entry.date);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return purchasesByUserId.values();
    }

    private void addEntryToPurchase(SanitizedEntry entry, Map<Integer, Purchase> purchasesByUserId) {
        Purchase purchase = purchasesByUserId.computeIfAbsent(entry.userId, (id)-> new Purchase(id, entry.userName));
        Product product = new Product(entry.productId, entry.productValue);
        Order order = new Order(entry.orderId, entry.date);

        if (purchase.containsOrder(entry.orderId)) {
            order = purchase.getOrder(entry.orderId);
        } else {
            purchase.addOrder(order);
        }

        order.addProduct(product);

    }

    private class SanitizedEntry {
        int userId;
        String userName;
        int orderId;
        int productId;
        BigDecimal productValue;
        String date;

        protected SanitizedEntry(String entry) {
            this.userId = Integer.parseInt(entry.substring(0, 10).trim());
            this.userName = entry.substring(10, 55).trim();
            this.orderId = Integer.parseInt(entry.substring(55, 65).trim());
            this.productId = Integer.parseInt(entry.substring(65, 75).trim());
            this.productValue = BigDecimal.valueOf(Double.parseDouble(entry.substring(75, 87).trim()));
            String rawDate = entry.substring(87);
            this.date = rawDate.substring(0, 4) + "-" + rawDate.substring(4, 6) + "-" + rawDate.substring(6);
        }
    }
}
