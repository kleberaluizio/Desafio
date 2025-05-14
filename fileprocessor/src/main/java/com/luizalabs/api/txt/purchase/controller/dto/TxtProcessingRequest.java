package com.luizalabs.api.txt.purchase.controller.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

public class TxtProcessingRequest {
    private final MultipartFile file;
    private final Integer filteredOrderId;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public TxtProcessingRequest(MultipartFile file, Integer filteredOrderId, LocalDate startDate, LocalDate endDate) {
        this.file = file;
        this.filteredOrderId = filteredOrderId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public InputStream getFileInputStream() throws IOException {
        return file.getInputStream();
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
}
