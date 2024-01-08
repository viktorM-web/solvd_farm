package com.solvd.farm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@UtilityClass
public class JSONParser {

    public static final String PATH = "src/main/resources/model/";

    public static Optional<Object> getObject(String fileName, Object obj){
        File fileXml = new File(PATH + fileName);
        Object result=null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();;
            result = objectMapper.readValue(fileXml, obj.getClass());
        } catch (IOException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(result);
    }
}
