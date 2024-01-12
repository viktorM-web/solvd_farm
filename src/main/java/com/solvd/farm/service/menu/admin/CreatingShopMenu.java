package com.solvd.farm.service.menu.admin;

import com.solvd.farm.domain.Shop;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.menu.IMenu;
import com.solvd.farm.util.Parser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class CreatingShopMenu implements IMenu {
    @Setter
    private Session session;

    @Override
    public void execute() {
        boolean exit = false;
        while (!exit) {
            log.info("you want to create shop " +
                     "\n by xml press [1]" +
                     "\n by xml(JAXB) press[2] " +
                     "\n by json press[3] " +
                     "\n if you want exit [0]");
            String requestForMenu = session.getRequestForMenu();
            switch (requestForMenu) {
                case "0" -> {
                    exit = true;
                    log.info("back to user menu");
                }
                case "2" -> {
                    Shop shop = new Shop();
                    boolean fileRed = false;
                    while (!fileRed) {
                        log.info("enter name xml file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = Parser.JAXB.parseTo(requestForMenu, shop);
                        if (maybeUser.isPresent()) {
                            shop = (Shop) maybeUser.get();
                            fileRed = true;
                        }
                    }
                    session.getImpl().getShopRepository().save(shop);

                    if (shop.getId() != null) {
                        log.info(shop + "was created");
                    } else {
                        log.info("failed to create ");
                    }
                    exit = true;
                }
                case "3" -> {
                    Shop shop = new Shop();
                    boolean fileRed = false;
                    while (!fileRed) {
                        log.info("enter name json file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = Parser.JSON.parseTo(requestForMenu, shop);
                        if (maybeUser.isPresent()) {
                            shop = (Shop) maybeUser.get();
                            fileRed = true;
                        }
                    }
                    session.getImpl().getShopRepository().save(shop);

                    if (shop.getId() != null) {
                        log.info(shop + "was created");
                    } else {
                        log.info("failed to create ");
                    }
                    exit = true;
                }
                case "1" -> {
                    Shop shop = new Shop();
                    boolean fileRed = false;
                    while (!fileRed) {
                        log.info("enter name xml file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = Parser.DOM.parseTo(requestForMenu, shop);
                        if (maybeUser.isPresent()) {
                            shop = (Shop) maybeUser.get();
                            fileRed = true;
                        }
                    }
                    session.getImpl().getShopRepository().save(shop);

                    if (shop.getId() != null) {
                        log.info(shop + "was created");
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
