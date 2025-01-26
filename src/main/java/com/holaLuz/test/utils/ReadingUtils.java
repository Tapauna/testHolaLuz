package com.holaLuz.test.utils;

import com.holaLuz.test.entity.SuspiciousReadingEntity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReadingUtils {

    public static String PATH_RESOURCES="src/main/resources/files/";
    public static String ERROR_MESSAGE="Error while saving results: ";
    public static String INFO_MESSAGE="Retrieve suspicious readings from file ";
    public static String NOT_FOUND_MESSAGE="File not found";
    public static String XML_EXTENSION=".xml";
    private static final String CSV_EXTENSION=".csv";
    private static final String PATH_RESULTS_FOLDER="/results/";
    private static final String CSV_HEADERS="Client,Month,Suspicious,Median";
    private static final String NO_VALUES_MESSAGE="No Suspicious Values";
    private static final String OK_MESSAGE="Results stored in ";

    /**
     *
     * @param readings
     * @return
     */
    public static String exportResultsToCsv(List<SuspiciousReadingEntity> readings)  {
        if(readings.isEmpty()){
            return NO_VALUES_MESSAGE;
        }
        String fileName="results_"+ LocalDate.now() +"_"+System.currentTimeMillis()+CSV_EXTENSION;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_RESOURCES+PATH_RESULTS_FOLDER+fileName))) {
            // Write the headers of the CSV File
            writer.write(CSV_HEADERS);
            writer.newLine();

            // Write the suspicious readings
            for (SuspiciousReadingEntity reading : readings) {
                writer.write(reading.getClientId() + "," + reading.getMonth() + "," + reading.getSuspiciousValue()+","+reading.getMedian());
                writer.newLine();
            }
        }
        catch (IOException e){
            return ERROR_MESSAGE+e.getMessage();
        }
        return OK_MESSAGE+PATH_RESOURCES+PATH_RESULTS_FOLDER+fileName;
    }
}
