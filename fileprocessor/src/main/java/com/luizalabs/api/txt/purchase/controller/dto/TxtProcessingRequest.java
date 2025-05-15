package com.luizalabs.api.txt.purchase.controller.dto;

import com.luizalabs.api.txt.purchase.exception.InvalidFilterParameterFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TxtProcessingRequest {
    private final MultipartFile file;
    private final Integer filteredOrderId;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private TxtProcessingRequest(MultipartFile file, Integer filteredOrderId, LocalDate startDate, LocalDate endDate) {
        this.file = file;
        this.filteredOrderId = filteredOrderId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static TxtProcessingRequest from(MultipartFile file, String filteredOrderIdStr, String startDateStr, String endDateStr) {
        StringBuilder errors = new StringBuilder();

        assertValidFileOrCollectError(file, errors);

        Integer filteredOrderId = parseOrderIdOrCollectError(filteredOrderIdStr, errors);
        LocalDate startDate = parseDateOrCollectError(startDateStr, errors, "start_date");
        LocalDate endDate = parseDateOrCollectError(endDateStr, errors, "end_date");

        assertValidDatesOrCollectError(startDate, endDate, errors);

        if (!errors.isEmpty()) {
            throw new InvalidFilterParameterFormatException(errors.toString());
        }

        return new TxtProcessingRequest(file, filteredOrderId, startDate, endDate);
    }

    public InputStream getFileInputStream() throws IOException {
        return file.getInputStream();
    }

    public String getFileName() {
        return file.getOriginalFilename();
    }

    public Integer getFilteredOrderId() {
        return filteredOrderId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    private static void assertValidFileOrCollectError(MultipartFile file, StringBuilder errors) {
        if (!file.getContentType().equals("text/plain")) {
            errors.append("Invalid file type: only plain text files (.txt) are allowed.").append("\n");
        }

        if (file.isEmpty()) {
            errors.append("File is empty").append("\n");
        }
    }

    private static Integer parseOrderIdOrCollectError(String orderIdStr, StringBuilder errors) {
        if (orderIdStr == null) return null;
        try {
            return Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            errors.append("Invalid value for 'order_id'. Expected type: Integer").append("\n");
        }
        return null;
    }

    private static LocalDate parseDateOrCollectError(String date, StringBuilder errors, String label) {
        if (date == null) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            errors.append("Invalid value for '").append(label).append("'. Expected format: yyyy-MM-dd").append("\n");
        }
        return null;
    }

    private static void assertValidDatesOrCollectError(LocalDate startDate, LocalDate endDate, StringBuilder errors) {
        boolean hasIncompleteDateRange = (startDate == null && endDate != null) || (startDate != null && endDate == null);
        if (hasIncompleteDateRange) {
            errors.append("Both 'start_date' and 'end_date' parameters must be either provided or both omitted.").append("\n");
        }

        if (!isValidDateRange(startDate, endDate)) {
            errors.append("Invalid date range: 'start_date' must be before or equal to 'end_date'.\n");
        }
    }

    private static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return true;
        return startDate.equals(endDate) || startDate.isBefore(endDate);
    }
}
