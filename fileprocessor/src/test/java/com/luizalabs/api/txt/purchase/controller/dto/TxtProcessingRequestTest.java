package com.luizalabs.api.txt.purchase.controller.dto;

import com.luizalabs.api.txt.purchase.factories.FileFactory;
import com.luizalabs.api.txt.purchase.exception.InvalidFilterParameterFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.web.multipart.MultipartFile;

class TxtProcessingRequestTest {

    @Test
    @DisplayName("Should throw InvalidFilterParameterFormatException when file type is not text/plain")
    void shouldThrowInvalidFilterParameterFormatException_WhenFileTypeIsNotPlainText() {
        MultipartFile file = FileFactory.InvalidContentTypeFile();

        var exception = Assertions.assertThrows(
                InvalidFilterParameterFormatException.class,
                () -> TxtProcessingRequest.from(file, null, null, null)
        );

        Assertions.assertEquals("Invalid file type: only plain text files (.txt) are allowed.\n", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidFilterParameterFormatException when file type is empty")
    void shouldThrowInvalidFilterParameterFormatException_WhenFileIsEmpty() {
        MultipartFile file = FileFactory.emptyFile();

        var exception = Assertions.assertThrows(
                InvalidFilterParameterFormatException.class,
                () -> TxtProcessingRequest.from(file, null, null, null)
        );

        Assertions.assertEquals("File is empty\n", exception.getMessage());
    }

    @ParameterizedTest(name ="{1}")
    @DisplayName("Should throw InvalidFilterParameterFormatException when order_id is not an integer - Type Provided: ")
    @CsvSource(value = {"text, String", "12.1, Double"}, nullValues = "null")
    void shouldThrowInvalidFilterParameterFormatException_WhenOrderIdIsNotAnInteger(String orderIdStr, String message) {
        MultipartFile validFile = FileFactory.validFile();

        var exception = Assertions.assertThrows(
                InvalidFilterParameterFormatException.class,
                () -> TxtProcessingRequest.from(validFile, orderIdStr, null, null)
        );

        Assertions.assertEquals("Invalid value for 'order_id'. Expected type: Integer\n", exception.getMessage());
    }

    @ParameterizedTest(name ="{2}")
    @DisplayName("Should throw InvalidFilterParameterFormatException when invalid: ")
    @CsvSource(
            value = {
                    "199X-12-01, 2025-05-15, start_date",
                    "1993-12-01, 2025-05-1, end_date"},
            nullValues = "null")
    void shouldThrowInvalidFilterParameterFormatException_WhenDatesAreInvalid(String startDate, String endDateStr, String label) {
        MultipartFile validFile = FileFactory.validFile();

        var exception = Assertions.assertThrows(
                InvalidFilterParameterFormatException.class,
                () -> TxtProcessingRequest.from(validFile,  null, startDate, endDateStr)
        );

        String message = String.format(
                """
                        Invalid value for '%s'. Expected format: yyyy-MM-dd
                        Both 'start_date' and 'end_date' parameters must be either provided or both omitted.
                        """,
                label);

        Assertions.assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidFilterParameterFormatException when dates period range is invalid")
    void shouldThrowInvalidFilterParameterFormatException_WhenInvalidDateRange() {
        MultipartFile validFile = FileFactory.validFile();
        String startDateStr = "2025-05-15";
        String endDateStr = "2025-05-14";

        var exception = Assertions.assertThrows(
                InvalidFilterParameterFormatException.class,
                () -> TxtProcessingRequest.from(validFile,  null, startDateStr, endDateStr)
        );

        Assertions.assertEquals("Invalid date range: 'start_date' must be before or equal to 'end_date'.\n", exception.getMessage());
    }

}