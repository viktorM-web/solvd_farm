package com.solvd.farm.util;

import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.Shop;
import com.solvd.farm.domain.User;
import com.solvd.farm.domain.enums.Role;
import com.solvd.farm.domain.enums.TypeOffer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@UtilityClass
public class DocumentReader {

    public static final String USER_XSD = "src/main/resources/model/user.xsd";
    private static final String OFFER_XSD = "src/main/resources/model/offer.xsd";
    private static final String SHOP_XSD = "src/main/resources/model/shop.xsd";
    public static final String PATH = "src/main/resources/model/";
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static Optional<Object> getDocument(String nameFile, Object obj) {
        File fileXml = new File(PATH + nameFile);
        String fileXsd;
        Function<Document, Object> function = null;

        if (obj instanceof User) {
            fileXsd = USER_XSD;
            function = DocumentReader::createUser;
        } else if (obj instanceof Offer) {
            fileXsd = OFFER_XSD;
            function = DocumentReader::createOffer;
        } else if (obj instanceof Shop) {
            fileXsd = SHOP_XSD;
            function = DocumentReader::createShop;
        } else {
            log.error("not correct class");
            return Optional.empty();
        }

        boolean validXML = isValidXML(PATH + nameFile, fileXsd);
        if (!validXML) {
            log.error("not valid xml");
            return Optional.empty();
        }

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileXml);
            return Optional.of(function.apply(doc));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error(e.getMessage());
            log.info("something go wrong try other file");
        }
        return Optional.empty();
    }

    private static boolean isValidXML(String pathXml, String pathXsd) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(pathXsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(pathXml)));
        } catch (IOException | SAXException e) {
            log.error("Exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    private static Object createShop(Document doc) {
        Shop shop = new Shop();
        NodeList nodeList = doc.getChildNodes();
        Node item = nodeList.item(0);
        NodeList childNodes = item.getChildNodes();

        String name = childNodes.item(1).getTextContent();
        String userId = childNodes.item(3).getAttributes().item(0).getTextContent();

        NodeList userNodes = childNodes.item(3).getChildNodes();

        String login = userNodes.item(1).getTextContent();
        String password = userNodes.item(3).getTextContent();
        String nameRole = userNodes.item(5).getTextContent();
        User user = new User(Long.valueOf(userId), login, password, Role.valueOf(nameRole));

        NodeList offers = childNodes.item(5).getChildNodes();
        List<Offer> listOffer = new ArrayList<>();
        for (int i = 0; i < offers.getLength(); i++) {
            if (offers.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            NodeList offerFields = offers.item(i).getChildNodes();
            Offer offer = new Offer();
            for (int j = 0; j < offerFields.getLength(); j++) {
                if (offers.item(j).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                String contextOffer = offerFields.item(j).getTextContent();
                switch (offerFields.item(j).getNodeName()) {
                    case "type" -> offer.setType(TypeOffer.valueOf(contextOffer));
                    case "description" -> offer.setDescription(contextOffer);
                    case "price" -> offer.setPrice(Double.valueOf(contextOffer));
                }
            }
            listOffer.add(offer);
        }
        return new Shop(null, name, user, listOffer);
    }

    private static Object createOffer(Document doc) {
        Offer offer = new Offer();
        NodeList nodeList = doc.getChildNodes();
        Node item = nodeList.item(0);
        NodeList childNodes = item.getChildNodes();

        String type = childNodes.item(1).getTextContent();
        String description = childNodes.item(3).getTextContent();
        Double price = Double.valueOf(childNodes.item(5).getTextContent());
        Long shopId = Long.valueOf(childNodes.item(7).getAttributes().item(0).getTextContent());
        return new Offer(null, TypeOffer.valueOf(type), description, price, new Shop(shopId, null, null, null));
    }

    private static User createUser(Document doc) {
        NodeList nodeList = doc.getChildNodes();
        Node item = nodeList.item(0);
        NodeList childNodes = item.getChildNodes();

        String login = childNodes.item(1).getTextContent();
        String password = childNodes.item(3).getTextContent();
        String nameRole = childNodes.item(5).getTextContent();
        return new User(null, login, password, Role.valueOf(nameRole));
    }

}
