package com.solvd.farm.service.menu.salesman;

import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.Shop;
import com.solvd.farm.domain.enums.TypeOffer;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.menu.IMenu;
import com.solvd.farm.util.Validator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class UpdatingOfferMenu implements IMenu {

    @Setter
    private Session session;

    private Map<Long, Shop> showAllShops() {
        List<Shop> allShops = session.getImpl().getShopRepository().findAll();
        return allShops.stream()
                .filter(shop -> shop.getUser().getId() == session.getUser().getId())
                .peek(shop -> log.info(shop.getName() + "[" + shop.getId() + "]"))
                .collect(Collectors.toMap(Shop::getId, shop -> shop));
    }

    private Map<Long, Offer> showAllOffers(Shop shop) {
        List<Offer> allOffers = session.getImpl().getOfferRepository().findAllByShop(shop.getId());
        return allOffers.stream()
                .peek(offer -> log.info(offer.getType() + " " + offer.getDescription() + " " + offer.getPrice() + "[" + offer.getId() + "]"))
                .collect(Collectors.toMap(Offer::getId, offer -> offer));
    }

    @Override
    public void execute() {
        Shop shop = null;
        boolean exit = false;
        while (!exit && shop == null) {
            Map<Long, Shop> shops = showAllShops();
            log.info("Enter shop's id which you want to manage \nor [0] if you want back to user menu");
            Integer requestForMenu = session.getRequestIntegerForMenu(Long.MAX_VALUE);
            if (requestForMenu == 0) {
                exit = true;
            } else {
                shop = shops.getOrDefault(Long.valueOf(requestForMenu), null);
                if (shop == null) {
                    log.info("shop not found \ntry again");
                }
            }
        }
        Offer offer = null;
        while (!exit && offer == null) {
            Map<Long, Offer> offers = showAllOffers(shop);
            log.info("Enter offer's id which you want to change \nor [0] if you want back to user menu");
            Integer requestForMenu = session.getRequestIntegerForMenu(Long.MAX_VALUE);
            if (requestForMenu == 0) {
                exit = true;
            } else {
                offer = offers.getOrDefault(Long.valueOf(requestForMenu), null);
                if (shop == null) {
                    log.info("offer not found \ntry again");
                } else {
                    log.info("what you want to change? \nprice [1]\ntype[2]\ndescription[3]");
                    requestForMenu = session.getRequestIntegerForMenu(3L);
                    switch (requestForMenu) {
                        case 1 -> {
                            log.info("original price " + offer.getPrice() + " enter value which you want to set");
                            boolean correctData = false;
                            while (!correctData) {
                                String request = session.getRequestForMenu();
                                if (Validator.isCorrectDouble(request)) {
                                    offer.setPrice(Double.valueOf(request));
                                    session.getImpl().getOfferRepository().update(offer);
                                    log.info("changes was saved");
                                    correctData = true;
                                } else {
                                    log.info("not correct data");
                                }
                            }
                        }
                        case 2 -> {
                            log.info("original type " + offer.getType() + " select value which you want to set");
                            boolean correctData = false;
                            while (!correctData) {
                                log.info("\nSEll [1]\nBUY [2]");
                                String request = session.getRequestForMenu();
                                if (request.equals("1")) {
                                    offer.setType(TypeOffer.SELL);
                                    session.getImpl().getOfferRepository().update(offer);
                                    log.info("changes was saved");
                                    correctData = true;
                                } else if (request.equals("2")) {
                                    offer.setType(TypeOffer.BUY);
                                    session.getImpl().getOfferRepository().update(offer);
                                    log.info("changes was saved");
                                    correctData = true;
                                } else {
                                    log.info("not correct data");
                                }
                            }
                        }
                        case 3 -> {
                            log.info("original description " + offer.getDescription() + " enter value which you want to set");
                            boolean correctData = false;
                            while (!correctData) {
                                String request = session.getRequestForMenu();
                                if (Validator.isCorrectDescription(request)) {
                                    offer.setDescription(request);
                                    session.getImpl().getOfferRepository().update(offer);
                                    log.info("changes was saved");
                                    correctData = true;
                                } else {
                                    log.info("not correct data");
                                }
                            }
                        }
                        default -> log.info("back to user menu");
                    }
                }
            }
        }
    }
}
