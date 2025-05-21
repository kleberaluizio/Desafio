package com.luizalabs.api.txt.purchase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File Processing (.txt)")
public interface FileProcessorSwaggerAnnotations {

    @Operation(summary = "Processes a .txt file containing purchase records and returns a JSON with normalized data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful processing"),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameter format | Invalid entries found in the file")
    })
    ResponseEntity<?> process(
            @Parameter(description = "File to be processed (.txt)", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Order ID (type: integer, optional)")
            @RequestParam(value="order_id", required = false) String orderIdStr,

            @Parameter(description = "Start date (format: YYYY-MM-DD, optional)")
            @RequestParam(value="start_date", required = false) String startDateStr,

            @Parameter(description = "End date (format: YYYY-MM-DD, optional)")
            @RequestParam(value="end_date", required = false) String endDateStr
    );
}
