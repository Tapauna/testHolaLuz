package com.holaLuz.test.controller;
import com.holaLuz.test.entity.ReadingEntity;
import com.holaLuz.test.mapper.ReadingMapper;
import com.holaLuz.test.service.ReadingService;
import com.holaLuz.test.utils.ReadingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/readings")
public class ReadingController {

    @Autowired
    private ReadingService readingService;

    private static final Logger logger = LoggerFactory.getLogger(ReadingController.class);

    /**
     * Given a file name, it collects suspicious readings in a CSV file, regardless of the extension of the input file.
     * @param fileName The file name
     * @return message with the status of the operation
     */
    @GetMapping("/readFile/{fileName}")
    public ResponseEntity<String> findSuspiciousValues(@PathVariable String fileName){
        try {
            logger.info(ReadingUtils.INFO_MESSAGE+fileName);
            //Read the file
            Stream<String> rawData=Files.lines(Paths.get(ReadingUtils.PATH_RESOURCES +fileName));
            //Depending on the file extension, the information is transformed to a common entity with its corresponding mapper.
            Map<String,List<ReadingEntity>> data=fileName.contains(ReadingUtils.XML_EXTENSION)? ReadingMapper.xmlToReadingEntity(rawData):
            ReadingMapper.csvToReadingEntity(rawData);
            //Find the suspicious values and store in a csv File
            String result=ReadingUtils.exportResultsToCsv(readingService.getSuspiciousReadings(data));
            if(result.contains(ReadingUtils.ERROR_MESSAGE)){
                logger.error(result);
                return ResponseEntity.internalServerError().body(result);
            }
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            logger.error(ReadingUtils.NOT_FOUND_MESSAGE);
            return ResponseEntity.badRequest().body(ReadingUtils.NOT_FOUND_MESSAGE);
        }
    }
}
