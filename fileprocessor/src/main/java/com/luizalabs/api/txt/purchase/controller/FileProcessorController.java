package com.luizalabs.api.txt.purchase.controller;

import com.luizalabs.api.txt.purchase.controller.dto.TxtProcessingRequest;
import com.luizalabs.api.txt.purchase.domain.Purchase;
import com.luizalabs.api.txt.purchase.service.FileProcessorServiceBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;

@RestController
@RequestMapping("api/file")
public class FileProcessorController {

    private final FileProcessorServiceBean service;

    public FileProcessorController(FileProcessorServiceBean service) {
        this.service = service;
    }

    @PostMapping("process")
    public ResponseEntity<?> process(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value="order_id", required = false) String orderIdStr,
            @RequestParam(value="start_date", required = false) String startDateStr,
            @RequestParam(value="end_date", required = false) String endDateStr)
    {
        StringBuilder errors = new StringBuilder();
        collectErrorIfEmptyFile(file, errors);
        Integer orderId = parseOrderIdOrCollectError(orderIdStr, errors);
        LocalDate startDate = parseDateOrCollectError(startDateStr, errors, "start_date");
        LocalDate endDate = parseDateOrCollectError(endDateStr, errors, "end_date");

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        TxtProcessingRequest request = new TxtProcessingRequest(file, orderId, startDate, endDate);
        Collection<Purchase> data = this.service.processFile(request);
        return ResponseEntity.ok(data);
    }

    private void collectErrorIfEmptyFile(MultipartFile file, StringBuilder errors) {
        if (file.isEmpty()) {
            errors.append("File is empty").append("\n");
        }
    }

    private Integer parseOrderIdOrCollectError(String orderIdStr, StringBuilder errors) {
        if (orderIdStr == null) return null;
        try {
            return Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            errors.append("Invalid value for 'order_id'. Expected type: Integer").append("\n");
        }
        return null;
    }

    private LocalDate parseDateOrCollectError(String date, StringBuilder errors, String label) {
        if (date == null) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            errors.append("Invalid value for '").append(label).append("'. Expected format: yyyy-MM-dd").append("\n");
        }
        return null;
    }
}
