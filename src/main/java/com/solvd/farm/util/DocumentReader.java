package com.solvd.farm.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@UtilityClass
public class DocumentReader {

    public static final String PATH = "src/main/resources/model/";
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static Optional<Document> getDocument(String nameFile) {
        Optional<Document> empty = Optional.empty();
        File file = new File(PATH + nameFile);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document parse = builder.parse(file);
            return Optional.of(parse);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error(e.getMessage());
            log.info("something go wrong try other file");
        }
        return empty;
    }
}
