package com.solvd.farm.service.menu.salesman;

import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.Shop;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.menu.IMenu;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ShowingOfferMenu implements IMenu {

    @Setter
    private Session session;

    private Map<Long, Shop> showAllShops() {
        List<Shop> allShops = session.getImpl().getShopRepository().findAll();
        return allShops.stream()
                .filter(shop -> shop.getUser().getId() == session.getUser().getId())
                .peek(shop -> log.info(shop.getName() + "[" + shop.getId() + "]"))
                .collect(Collectors.toMap(Shop::getId, shop -> shop));
    }

    private void showAllOffers(Shop shop) {
        List<Offer> allOffers = session.getImpl().getOfferRepository().findAllByShop(shop.getId());
        for (Offer offer : allOffers) {
            log.info(offer.getDescription() + offer.getPrice() + offer.getShop().getName() + "[" + offer.getId() + "]");
        }
    }

    @Override
    public void execute() {
        Shop shop = null;
        while (shop == null) {
            Map<Long, Shop> shops = showAllShops();
            log.info("Enter shop's id which you want to manage \nor [0] if you want back to user menu");
            Integer requestForMenu = session.getRequestIntegerForMenu(Long.MAX_VALUE);
            if (requestForMenu == 0) {
                break;
            }
            shop = shops.getOrDefault(Long.valueOf(requestForMenu), null);
            if (shop == null) {
                log.info("shop not found \ntry again");
            } else {
                showAllOffers(shop);
                log.info("If you want select other shop press [enter] \nor [0] if you want back to user menu");
                String request = session.getRequestForMenu();
                if (!request.equals("0")) {
                    shop = null;
                }
            }
        }
    }
}
