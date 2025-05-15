package com.luizalabs.api.txt.purchase.service;

import com.luizalabs.api.txt.purchase.controller.dto.TxtProcessingRequest;
import com.luizalabs.api.txt.purchase.exception.InvalidFileEntriesException;
import com.luizalabs.api.txt.purchase.service.parameters.InputErrors;
import com.luizalabs.api.txt.purchase.domain.Order;
import com.luizalabs.api.txt.purchase.domain.Product;
import com.luizalabs.api.txt.purchase.domain.Purchase;
import com.luizalabs.api.txt.purchase.service.parameters.SanitizedEntryParams;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FileProcessorService {

    public Collection<Purchase> processFile(TxtProcessingRequest request) {
        Map<Integer, Purchase> purchasesByUserId = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getFileInputStream()))) {
            InputErrors fileErrors = new InputErrors(request.getFileName());

            String rawEntry;
            while ((rawEntry = reader.readLine()) != null) {
                SanitizedEntryParams entry = getSanitizedEntryOrCollectError(rawEntry, fileErrors);

                if (!fileErrors.isEmpty()) {
                    continue; //Once an error was found, should not process any entry, just collect errors
                }
                if (isOrderIdIgnoredByFilter(entry.orderId, request.getFilteredOrderId())) {
                    continue;
                }
                if (isDateIgnoredByDateRangeFilter(entry.date, request.getStartDate(), request.getEndDate())) {
                    continue;
                }

                addEntryToUserPurchase(entry, purchasesByUserId);
            }

            if (!fileErrors.isEmpty()){
                throw new InvalidFileEntriesException(fileErrors.getErrorDescription());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return purchasesByUserId.values();
    }

    private boolean isOrderIdIgnoredByFilter(int entryOrderId, Integer filteredOrderId) {
        if (filteredOrderId == null) {
            return false; // If no filter is defined, do not ignore any orderId
        }
        return !filteredOrderId.equals(entryOrderId);
    }

    private boolean isDateIgnoredByDateRangeFilter(String entryDateStr, LocalDate startDate, LocalDate endDate ) {
        if (startDate == null && endDate == null) {
            return false; // If no filter is defined, do not ignore any entry date
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate entryDate = LocalDate.parse(entryDateStr, formatter);
        boolean isBetweenStartAndEndDate = entryDate.isAfter(startDate) && entryDate.isBefore(endDate);
        boolean isInRange = (entryDate.equals(startDate) || entryDate.equals(endDate) || isBetweenStartAndEndDate);
        return !isInRange;
    }

    private void addEntryToUserPurchase(SanitizedEntryParams entry, Map<Integer, Purchase> purchasesByUserId) {
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
    
    private SanitizedEntryParams getSanitizedEntryOrCollectError(String rawEntry, InputErrors fileErrors) {
        try{
            SanitizedEntryParams entry = new SanitizedEntryParams(rawEntry);
            fileErrors.incrementCurrentLine();
            return entry;
        } catch (Exception e) {
            fileErrors.addCurrentLineToErrorListAndIncrement();
        }
        return null;
    }
}
