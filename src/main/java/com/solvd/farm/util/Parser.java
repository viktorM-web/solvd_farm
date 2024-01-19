package com.solvd.farm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.Shop;
import com.solvd.farm.domain.User;
import com.solvd.farm.domain.enums.Role;
import com.solvd.farm.domain.enums.TypeOffer;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
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
public enum Parser {

    JSON() {
        public Optional<Object> parseTo(String fileName, Object obj) {
            File fileXml = new File(PATH + fileName);
            Object result = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                result = objectMapper.readValue(fileXml, obj.getClass());
            } catch (IOException e) {
                log.error(e.getMessage());
                return Optional.empty();
            }
            return Optional.of(result);
        }
    },
    JAXB() {
        public Optional<Object> parseTo(String fileName, Object obj) {
            File fileXml = new File(PATH + fileName);
            Object result = null;
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
    },
    DOM() {
        public Optional<Object> parseTo(String fileName, Object obj) {
            File fileXml = new File(PATH + fileName);
            Function<Document, Object> function = null;

            if (obj instanceof User) {
                function = Parser::createUser;
            } else if (obj instanceof Offer) {
                function = Parser::createOffer;
            } else if (obj instanceof Shop) {
                function = Parser::createShop;
            } else {
                log.error("not correct class");
                return Optional.empty();
            }

            boolean validXML = isValidXML(PATH + fileName, obj);
            if (!validXML) {
                log.error("not valid xml");
                return Optional.empty();
            }

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(fileXml);
                return Optional.of(function.apply(doc));
            } catch (ParserConfigurationException | SAXException | IOException e) {
                log.error(e.getMessage());
                log.info("something go wrong try other file");
            }
            return Optional.empty();
        }
    };

    public static final String USER_XSD = "src/main/resources/model/user.xsd";
    private static final String OFFER_XSD = "src/main/resources/model/offer.xsd";
    private static final String SHOP_XSD = "src/main/resources/model/shop.xsd";
    private static final String PATH = "src/main/resources/model/";

    abstract public Optional<Object> parseTo(String fileName, Object obj);

    private static boolean isValidXML(String pathXml, Object obj) {
        String fileXsd;
        if (obj instanceof User) {
            fileXsd = USER_XSD;
        } else if (obj instanceof Offer) {
            fileXsd = OFFER_XSD;
        } else if (obj instanceof Shop) {
            fileXsd = SHOP_XSD;
        } else {
            log.error("not correct class");
            return false;
        }

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(fileXsd));
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

        for (int j = 0; j < nodeList.getLength(); j++) {
            if (nodeList.item(j).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (nodeList.item(j).getNodeName().equals("shop")) {

                NamedNodeMap attributes = nodeList.item(j).getAttributes();
                Node id = attributes.getNamedItem("id");
                if (id != null && !id.getTextContent().isBlank()) {
                    shop.setId(Long.valueOf(attributes.getNamedItem("id").getTextContent()));
                }

                NodeList shopFields = nodeList.item(j).getChildNodes();
                for (int i = 0; i < shopFields.getLength(); i++) {
                    if (shopFields.item(i).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    switch (shopFields.item(i).getNodeName()) {
                        case "name" -> shop.setName(shopFields.item(i).getTextContent());
                        case "user" -> shop.setUser(parseToUser(shopFields.item(i)));
                        case "offers" -> {
                            List<Offer> listOffer = new ArrayList<>();
                            NodeList offers = shopFields.item(i).getChildNodes();
                            for (int c = 0; c < offers.getLength(); c++) {
                                if (offers.item(c).getNodeType() != Node.ELEMENT_NODE) {
                                    continue;
                                }
                                if (offers.item(c).getNodeName().equals("offer")) {
                                    listOffer.add(parseToOffer(offers.item(c)));
                                }
                            }
                            shop.setOffers(listOffer);
                        }
                    }
                }
                return shop;
            }
        }
        return shop;
    }

    private static Object createOffer(Document doc) {
        NodeList nodeList = doc.getChildNodes();
        Offer offer = new Offer();
        for (int j = 0; j < nodeList.getLength(); j++) {
            if (nodeList.item(j).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (nodeList.item(j).getNodeName().equals("offer")) {
                return parseToOffer(nodeList.item(j));
            }
        }
        return offer;
    }

    private static Offer parseToOffer(Node node) {
        Offer offer = new Offer();
        NamedNodeMap attributes = node.getAttributes();
        Node id = attributes.getNamedItem("id");
        if (id != null && !id.getTextContent().isBlank()) {
            offer.setId(Long.valueOf(attributes.getNamedItem("id").getTextContent()));
        }

        NodeList offerFields = node.getChildNodes();
        for (int i = 0; i < offerFields.getLength(); i++) {
            if (offerFields.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            switch (offerFields.item(i).getNodeName()) {
                case "price" -> offer.setPrice(Double.valueOf(offerFields.item(i).getTextContent()));
                case "description" -> offer.setDescription(offerFields.item(i).getTextContent());
                case "type" -> offer.setType(TypeOffer.valueOf(offerFields.item(i).getTextContent()));
            }
        }
        return offer;
    }

    private static User createUser(Document doc) {
        NodeList nodeList = doc.getChildNodes();
        User user = new User();
        for (int j = 0; j < nodeList.getLength(); j++) {
            if (nodeList.item(j).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (nodeList.item(j).getNodeName().equals("user")) {
                return parseToUser(nodeList.item(j));
            }
        }
        return user;
    }

    private static User parseToUser(Node node) {
        User user = new User();
        NamedNodeMap attributes = node.getAttributes();
        Node id = attributes.getNamedItem("id");

        if (id != null && !id.getTextContent().isBlank()) {
            user.setId(Long.valueOf(attributes.getNamedItem("id").getTextContent()));
        }

        NodeList userFields = node.getChildNodes();
        for (int i = 0; i < userFields.getLength(); i++) {
            if (userFields.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            switch (userFields.item(i).getNodeName()) {
                case "login" -> user.setLogin(userFields.item(i).getTextContent());
                case "password" -> user.setPassword(userFields.item(i).getTextContent());
                case "role" -> user.setRole(Role.valueOf(userFields.item(i).getTextContent()));
            }
        }
        return user;
    }
}
