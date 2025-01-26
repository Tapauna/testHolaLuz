package com.holaLuz.test.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.holaLuz.test.entity.ReadingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Stream;

public class ReadingMapper {

    private static final String FIRST_LINE_XML="<?xml version=\"1.0\"?>";
    private static final String SECOND_LINE_XML="<readings>";
    private static final String LAST_LINE_XML="</readings>";
    private static final String CSV_HEADER="client";
    private static final String PARSING_ERROR_MSG="Error when parsing xml line";
    private static final Logger logger = LoggerFactory.getLogger(ReadingMapper.class);

    /**
     * Map an xml file to a ReadingEntity map
     * @param rawData The information from the XML file
     * @return a map whose key is the clientId and value the list of reads of that clientId
     */
    public static Map<String,List<ReadingEntity>> xmlToReadingEntity(Stream<String> rawData){
        Map<String,List<ReadingEntity>> readingsMap=new HashMap<>();
        //use xmlMapper to map directly the xml lines to the entity
        XmlMapper xmlMapper= new XmlMapper();
        rawData.forEach(line ->{
            try {
                if(line.equals(FIRST_LINE_XML) || line.equals(SECOND_LINE_XML) || line.equals(LAST_LINE_XML)){
                    return;
                }
                ReadingEntity entity=xmlMapper.readValue(line, ReadingEntity.class);
                List<ReadingEntity> readings=readingsMap.get(entity.getClient());
                if(readings==null){
                    readings=new ArrayList<>();
                    readings.add(entity);
                    readingsMap.put(entity.getClient(),readings);
                }
                else{
                    readings.add(entity);
                }
            } catch (JsonProcessingException e) {
                logger.error(PARSING_ERROR_MSG+" "+line);
            }
        });
        return readingsMap;
    }

    /**
     * Map an csv file to a ReadingEntity map
     * @param rawData The information from the csv file
     * @return a map whose key is the clientId and value the list of reads of that clientId
     */
    public static Map<String,List<ReadingEntity>> csvToReadingEntity(Stream<String> rawData) {
        Map<String, List<ReadingEntity>> readingsMap = new HashMap<>();
            rawData.forEach(line -> {
                if (line.contains(CSV_HEADER)) {
                    return;
                }
                    String[] fields=line.split(",");
                    ReadingEntity entity=mapReadingEntity(fields);
                    if(entity==null){
                        return;
                    }
                    List<ReadingEntity> readings = readingsMap.get(entity.getClient());
                    if (readings == null) {
                        readings= new ArrayList<>();
                        readings.add(entity);
                        readingsMap.put(entity.getClient(), readings);
                    }
                    else{
                        readings.add(entity);
                    }
            });
            return readingsMap;
    }

    /**
     * Map new ReadingEntity instance
     * @param fields the fields from the file
     * @return the ReadingEntity instance
     */
    private static ReadingEntity mapReadingEntity(String[] fields){
        //if fields is below 3, something in the line is wrong, ignore it
        if(fields.length<3){
            return null;
        }
        ReadingEntity entity = new ReadingEntity();
        entity.setClient(fields[0]);
        entity.setPeriod(fields[1]);
        entity.setReading(Integer.valueOf(fields[2]));
        return entity;
    }
}
