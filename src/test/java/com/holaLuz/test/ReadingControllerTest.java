
package com.holaLuz.test;

import com.holaLuz.test.controller.ReadingController;
import com.holaLuz.test.entity.SuspiciousReadingEntity;
import com.holaLuz.test.service.ReadingService;
import com.holaLuz.test.utils.ReadingUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadingControllerTest {

    @Mock
    private ReadingService readingService;

    @InjectMocks
    private ReadingController readingController;

    private static final String VALID_CSV_FILE = "2016-readings.csv";
    private static final String VALID_XML_FILE = "2016-readings.xml";
    private static final String INVALID_FILE = "invalid.txt";
    private static final String FILE_PATH = "src/test/resources/files/";

    @BeforeEach
    public void setup() {
        ReadingUtils.PATH_RESOURCES = FILE_PATH;
        ReadingUtils.XML_EXTENSION = ".xml";
        ReadingUtils.ERROR_MESSAGE = "Error";
        ReadingUtils.NOT_FOUND_MESSAGE = "File not found";
        ReadingUtils.INFO_MESSAGE = "Processing file: ";

    }

    @Test public void testFindSuspiciousValues_withValidCsvFile() throws IOException {
        List<SuspiciousReadingEntity> suspiciousList=new ArrayList<>();
        SuspiciousReadingEntity suspicious= new SuspiciousReadingEntity();
        suspicious.setClientId("1223");
        suspicious.setMonth("1232");
        suspicious.setSuspiciousValue(1232131);
        suspicious.setMedian(new BigDecimal(43534534));
        suspiciousList.add(suspicious);

        when(readingService.getSuspiciousReadings(any(Map.class))).thenReturn(suspiciousList);
        ResponseEntity<String> response = readingController.findSuspiciousValues(VALID_CSV_FILE);
        Assertions.assertTrue(response.getBody().contains("Results stored in"));
    }

    @Test public void testFindSuspiciousValues_withValidXmlFile() throws IOException {
        List<SuspiciousReadingEntity> suspiciousList=new ArrayList<>();
        SuspiciousReadingEntity suspicious= new SuspiciousReadingEntity();
        suspicious.setClientId("1223");
        suspicious.setMonth("1232");
        suspicious.setSuspiciousValue(1232131);
        suspicious.setMedian(new BigDecimal(43534534));
        suspiciousList.add(suspicious);

        when(readingService.getSuspiciousReadings(any(Map.class))).thenReturn(suspiciousList);
        ResponseEntity<String> response = readingController.findSuspiciousValues(VALID_XML_FILE);
        Assertions.assertTrue(response.getBody().contains("Results stored in"));
    }

    @Test
    public void testFindSuspiciousValues_withFileNotFound() throws IOException {
        ResponseEntity<String> response = readingController.findSuspiciousValues(INVALID_FILE);
        assertEquals(ResponseEntity.badRequest().body(ReadingUtils.NOT_FOUND_MESSAGE), response);
    }
}
