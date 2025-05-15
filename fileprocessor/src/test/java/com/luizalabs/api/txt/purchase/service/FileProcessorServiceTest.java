package com.luizalabs.api.txt.purchase.service;

import com.luizalabs.api.txt.purchase.controller.dto.TxtProcessingRequest;
import com.luizalabs.api.txt.purchase.domain.Purchase;
import com.luizalabs.api.txt.purchase.exception.InvalidFileEntriesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;


class FileProcessorServiceTest {
    private FileProcessorService service;

    @BeforeEach
    void setup() {
        service = new FileProcessorService();
    }

    @Test
    @DisplayName("Should return purchases when entries are valid")
    void ShouldReturnPurchases_WhenValidEntries() throws Exception {
        String validContent = getFileValidContent();
        TxtProcessingRequest request = mockRequestWithContent(validContent, null, null, null);
        Collection<Purchase> purchases = service.processFile(request);
        Optional<Purchase> johns = purchases.stream().filter(p -> p.getUserId() == 151).findFirst();

        Assertions.assertAll(
                ()-> Assertions.assertEquals(5, purchases.size()),
                ()-> Assertions.assertTrue(johns.isPresent()),
                ()-> Assertions.assertEquals(2, johns.get().getOrders().size())
        );
    }

    @Test
    @DisplayName("Should Throw InvalidFileEntriesException when file contains invalid entries")
    void ShouldThrowException_WhenInvalidEntries() throws Exception{
        String content = """
            0000000064                               Reggie Reinger00000006090000000006     1203.7420210725
            Jan Johns00000014000000000004      548.5820211106
            0000000064                               Reggie Reinger00000006100000000001     1330.1520210522
            XXX0004                             Cruz Greenfelder00000000380000000003     1363.1120210924
            0000000108Jacquelyne Bosco   1320.8720210901
            """;

        TxtProcessingRequest request = mockRequestWithContent(content, null, null, null);
        var exception = Assertions.assertThrows(
                InvalidFileEntriesException.class,
                () -> service.processFile(request)
        );

        Assertions.assertTrue(exception.getMessage().contains("2, 4, 5"));
    }

    @Test
    @DisplayName("Should ignore non matching entries when filteredOrderId is provided")
    void ShouldIgnoreNonMatchingEntries_WhenFilteredOrderIdProvided() throws Exception {
        String validContent = getFileValidContent();
        int orderId = 609; //Reggie Reinger order id
        int otherOrderId = 996; // Jacquelyne Bosco order id

        TxtProcessingRequest request = mockRequestWithContent(validContent, orderId, null, null);
        Collection<Purchase> result = service.processFile(request);
        Purchase purchase = result.iterator().next();

        Assertions.assertAll(
                ()-> Assertions.assertEquals(1, result.size()),
                ()-> Assertions.assertEquals(1, purchase.getOrders().size()),
                ()-> Assertions.assertEquals("Reggie Reinger", purchase.getUserName()),
                ()-> Assertions.assertTrue(purchase.containsOrder(orderId)),
                ()-> Assertions.assertFalse(purchase.containsOrder(otherOrderId))
        );
    }

    @Test
    @DisplayName("Should Only include purchases with dates in range when date range provided")
    void ShouldOnlyIncludeDatesInRange_WhenDateRangeFilterProvided() throws Exception {
        String validContent = getFileValidContent();

        TxtProcessingRequest request = mockRequestWithContent(
                validContent,
                null,
                LocalDate.parse("2021-09-02"),
                LocalDate.parse("2021-10-08"));
        Collection<Purchase> result = service.processFile(request);

        Assertions.assertAll(
                ()-> Assertions.assertEquals(2, result.size()),
                ()-> Assertions.assertFalse(result.stream().anyMatch(purchase -> purchase.containsOrder(609))),   // Reggie Reinger
                ()-> Assertions.assertFalse(result.stream().anyMatch(purchase -> purchase.containsOrder(1400))),  // Jan Johns
                ()-> Assertions.assertFalse(result.stream().anyMatch(purchase -> purchase.containsOrder(610))),   // Reggie Reinger
                ()-> Assertions.assertTrue(result.stream().anyMatch(purchase -> purchase.containsOrder(38))),     // Cruz Greenfelder
                ()-> Assertions.assertTrue(result.stream().anyMatch(purchase -> purchase.containsOrder(1398))),   // Jan Johns
                ()-> Assertions.assertFalse(result.stream().anyMatch(purchase -> purchase.containsOrder(996)))    // Jacquelyne Bosco
        );
    }

    @Test
    @DisplayName("Should Throw RuntimeException when IOException is Thrown")
    void ShouldThrowRuntimeException_WhenThrowsIOException() throws Exception {
        InputStream errorStream = Mockito.mock(InputStream.class);
        Mockito.when(errorStream.read(Mockito.any())).thenThrow(new IOException("fail"));

        TxtProcessingRequest request = Mockito.mock(TxtProcessingRequest.class);
        Mockito.when(request.getFileInputStream()).thenReturn(errorStream);
        Mockito.when(request.getFileName()).thenReturn("test.txt");

        Assertions.assertThrows(RuntimeException.class, () -> service.processFile(request));
    }

    private String getFileValidContent() {
        return """
            0000000064                               Reggie Reinger00000006090000000006     1203.7420210725
            0000000151                                    Jan Johns00000014000000000004      548.5820211106
            0000000064                               Reggie Reinger00000006100000000001     1330.1520210522
            0000000004                             Cruz Greenfelder00000000380000000003     1363.1120210924
            0000000151                                    Jan Johns00000013980000000005      192.6920210911
            0000000108                             Jacquelyne Bosco00000009960000000004     1320.8720210901
            0000000002                               Dr. Earl Walsh00000000230000000006     1909.3720211127
            0000000002                               Dr. Earl Walsh00000000230000000004      605.7420211127
            """;
    }

    private TxtProcessingRequest mockRequestWithContent(String content, Integer orderId, LocalDate startDate, LocalDate endDate) throws Exception {
        TxtProcessingRequest request = Mockito.mock(TxtProcessingRequest.class);
        InputStream stream = new ByteArrayInputStream(content.getBytes());
        Mockito.when(request.getFileInputStream()).thenReturn(stream);
        Mockito.when(request.getFileName()).thenReturn("mock.txt");
        Mockito.when(request.getFilteredOrderId()).thenReturn(orderId);
        Mockito.when(request.getStartDate()).thenReturn(startDate);
        Mockito.when(request.getEndDate()).thenReturn(endDate);
        return request;
    }
}