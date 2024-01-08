package com.solvd.farm.util;

import com.solvd.farm.domain.Shop;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Optional;

@Slf4j
@UtilityClass
public class JAXBParser {

    public static final String PATH = "src/main/resources/model/";

    public static Optional<Object> getObject(String fileName, Object obj){
        File fileXml = new File(PATH + fileName);
        Object result=null;
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            result = unmarshaller.unmarshal(fileXml);

        } catch (JAXBException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(result);
    }
}
