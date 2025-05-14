package com.luizalabs.api.txt.purchase.controller;

import com.luizalabs.api.txt.purchase.domain.OptionalFilters;
import com.luizalabs.api.txt.purchase.domain.Purchase;
import com.luizalabs.api.txt.purchase.service.FileProcessorServiceBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam(value="order_id", required = false) Integer orderId,
            @RequestParam(value="start_date", required = false) String startDate,
            @RequestParam(value="end_date", required = false) String endDate)
    {
        OptionalFilters filters = new OptionalFilters(orderId, startDate, endDate);
        Collection<Purchase> data = this.service.processFile(file, filters);
        return ResponseEntity.ok(data);
    }

}
