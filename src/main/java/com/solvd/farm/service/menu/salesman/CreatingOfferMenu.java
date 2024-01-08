package com.solvd.farm.service.menu.salesman;

import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.Shop;
import com.solvd.farm.domain.enums.TypeOffer;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.menu.IMenu;
import com.solvd.farm.util.DocumentReader;
import com.solvd.farm.util.JAXBParser;
import com.solvd.farm.util.Validator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class CreatingOfferMenu implements IMenu {

    @Setter
    private Session session;

    private Map<Long, Shop> showAllShops() {
        List<Shop> allShops = session.getImpl().getShopRepository().findAll();
        return allShops.stream()
                .filter(shop -> shop.getUser().getId() == session.getUser().getId())
                .peek(shop -> log.info(shop.getName() + "[" + shop.getId() + "]"))
                .collect(Collectors.toMap(Shop::getId, shop -> shop));
    }

    @Override
    public void execute() {
        boolean exit = false;
        while (!exit){
            log.info("how you want create offer \n by xml press [1]\n by hand press[2] \nby xml(JAXB) press [3] \n if you want exit [0]");
            String requestForMenu = session.getRequestForMenu();
            switch (requestForMenu) {
                case "0" -> {
                    exit = true;
                    log.info("back to user menu");
                }
                case "2" -> {
                    Shop shop = null;
                    while (!exit && shop == null) {
                        Map<Long, Shop> shops = showAllShops();
                        log.info("Enter shop's id which you want to manage \nor [0] if you want back to user menu");
                        Integer requestInt = session.getRequestIntegerForMenu(Long.MAX_VALUE);
                        if (requestInt == 0) {
                            exit = true;
                        } else {
                            shop = shops.getOrDefault(Long.valueOf(requestInt), null);
                            if (shop == null) {
                                log.info("shop not found \ntry again");
                            }
                        }
                    }
                    TypeOffer typeOffer = null;
                    while (!exit && typeOffer == null) {
                        log.info("select type offer");
                        boolean correctData = false;
                        while (!correctData) {
                            log.info("\nSEll [1]\nBUY [2]\nexit [0]");
                            String request = session.getRequestForMenu();
                            switch (request) {
                                case "1" -> {
                                    typeOffer = TypeOffer.SELL;
                                    correctData = true;
                                }
                                case "2" -> {
                                    typeOffer = TypeOffer.BUY;
                                    correctData = true;
                                }
                                case "0" -> {
                                    correctData = true;
                                    exit = true;
                                    log.info("back to user menu");
                                }
                                default -> log.info("not correct data");
                            }
                        }
                    }
                    String description = null;
                    while (!exit && description == null) {
                        log.info("enter description offer \nor [0]if you want back to user menu");
                        boolean correctData = false;
                        while (!correctData) {
                            String request = session.getRequestForMenu();
                            if (request.equals("0")) {
                                correctData = true;
                                exit = true;
                            } else if (Validator.isCorrectDescription(request)) {
                                description = request;
                                correctData = true;
                            } else {
                                log.info("not correct data");
                            }
                        }
                    }
                    Double price = null;
                    while (!exit && price == null) {
                        log.info("enter price offer \nor [0]if you want back to user menu");
                        boolean correctData = false;
                        while (!correctData) {
                            String request = session.getRequestForMenu();
                            if (request.equals("0")) {
                                correctData = true;
                                exit = true;
                            } else if (Validator.isCorrectDouble(request)) {
                                price = Double.valueOf(request);
                                correctData = true;
                            } else {
                                log.info("not correct data");
                                log.info("enter price offer \nor [0]if you want back to user menu");
                            }
                        }
                    }
                    if (!exit) {
                        Offer offer = new Offer(null, typeOffer, description, price, shop);
                        session.getImpl().getOfferRepository().save(offer);
                        if (offer.getId() != null) {
                            log.info(offer + "was created");
                        }
                    }
                    exit=true;
                }
                case "1" -> {
                    Offer offer = new Offer();
                    boolean fileRed = false;
                    while (!fileRed) {
                        log.info("enter name xml file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = DocumentReader.getDocument(requestForMenu, offer);
                        if (maybeUser.isPresent()) {
                            offer = (Offer) maybeUser.get();
                            fileRed = true;
                        }
                    }
                    session.getImpl().getOfferRepository().save(offer);

                    if (offer.getId() != null) {
                        log.info(offer + "was created");
                    } else {
                        log.info("failed to create ");
                    }
                    exit = true;
                }
                case "3" -> {
                    Offer offer = new Offer();
                    boolean fileRed = false;
                    while (!fileRed) {
                        log.info("enter name xml file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = JAXBParser.getObject(requestForMenu, offer);
                        if (maybeUser.isPresent()) {
                            offer = (Offer) maybeUser.get();
                            fileRed = true;
                        }
                    }
                    session.getImpl().getOfferRepository().save(offer);

                    if (offer.getId() != null) {
                        log.info(offer + "was created");
                    } else {
                        log.info("failed to create ");
                    }
                    exit = true;
                }
                default -> log.info("not correct data");
            }
        }
    }
}
