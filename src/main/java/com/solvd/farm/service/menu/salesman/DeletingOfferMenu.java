package com.solvd.farm.service.menu.salesman;

import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.Shop;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.forAbstractFactory.IMenuMessage;
import com.solvd.farm.service.forAbstractFactory.ISessionInfo;
import com.solvd.farm.service.forAbstractFactory.SalesmanMenuMessage;
import com.solvd.farm.service.forAbstractFactory.SalesmanSessionInfo;
import com.solvd.farm.service.menu.IMenu;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DeletingOfferMenu implements IMenu {

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
    public IMenuMessage execute() {
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
            log.info("Enter offer's id which you want to delete \nor [0] if you want back to user menu");
            Integer requestForMenu = session.getRequestIntegerForMenu(Long.MAX_VALUE);
            if (requestForMenu == 0) {
                exit = true;
            } else {
                offer = offers.getOrDefault(Long.valueOf(requestForMenu), null);
                if (offer == null) {
                    log.info("offer not found \ntry again");
                } else {
                    if (session.getImpl().getOfferRepository().delete(offer.getId())) {
                        log.info(offer + "was deleted");
                    }
                }
            }
        }
        return new SalesmanMenuMessage("back to user menu");
    }

    @Override
    public ISessionInfo setSession(Session session) {
        this.session = session;
        return new SalesmanSessionInfo(this.session);
    }
}
