package com.luizalabs.api.txt.purchase.controller;

import com.luizalabs.api.txt.purchase.controller.dto.TxtProcessingRequest;
import com.luizalabs.api.txt.purchase.domain.Purchase;
import com.luizalabs.api.txt.purchase.exception.InvalidFileEntriesException;
import com.luizalabs.api.txt.purchase.exception.InvalidFilterParameterFormatException;
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
        try {
            TxtProcessingRequest request = TxtProcessingRequest.from(file, orderIdStr, startDateStr, endDateStr);
            Collection<Purchase> data = this.service.processFile(request);
            return ResponseEntity.ok(data);
        }catch (InvalidFilterParameterFormatException | InvalidFileEntriesException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
