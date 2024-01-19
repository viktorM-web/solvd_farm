package com.solvd.farm.service.menu.farmer;

import com.solvd.farm.domain.Farm;
import com.solvd.farm.domain.Item;
import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.enums.TypeItem;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.forAbstractFactory.FarmerMenuMessage;
import com.solvd.farm.service.forAbstractFactory.FarmerSessionInfo;
import com.solvd.farm.service.forAbstractFactory.IMenuMessage;
import com.solvd.farm.service.forAbstractFactory.ISessionInfo;
import com.solvd.farm.service.menu.IMenu;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class SellingItemMenu implements IMenu {

    private Farm farm;
    private Session session;

    private void showAllOffers() {
        List<Offer> allBuyingItem = session.getImpl().getOfferRepository().findAllBuyingItem();
        for (Offer offer : allBuyingItem) {
            log.info(offer.getDescription() + offer.getPrice() + offer.getShop().getName() + "[" + offer.getId() + "]");
        }
    }

    private void showAllFarms() {
        List<Farm> allFarms = session.getImpl().getFarmRepository().findAll();
        for (Farm farm : allFarms) {
            if (farm.getUser().getId() == session.getUser().getId())
                log.info(farm.getName() + farm.getBudget() + "[" + farm.getId() + "]");
        }
    }

    @Override
    public IMenuMessage execute() {
        boolean exit = false;
        while (!exit && farm == null) {
            showAllFarms();
            log.info("Enter farm's id which you want to manage \nor [0] if you want back to user menu");
            String requestForMenu = session.getRequestForMenu();
            if (requestForMenu.equals("0")) {
                exit = true;
                return new FarmerMenuMessage("back to user menu");
            }
            Optional<Farm> maybeFarm = session.getImpl().getFarmRepository().findById(Long.valueOf(requestForMenu));
            if (maybeFarm.isEmpty()) {
                log.info("farm not found \ntry again");
            } else {
                farm = maybeFarm.get();
            }
        }
        Offer offer = null;
        TypeItem type = null;
        while (!exit && (offer == null || type == null)) {
            showAllOffers();
            log.info("Enter offer's id which you want to sell \nor [0] if you want back to user menu");
            String requestForMenu = session.getRequestForMenu();
            if (requestForMenu.equals("0")) {
                exit = true;
                return new FarmerMenuMessage("back to user menu");
            }
            Optional<Offer> maybeOffer = session.getImpl().getOfferRepository().findById(Long.valueOf(requestForMenu));
            if (maybeOffer.isEmpty()) {
                log.info("offer not found \ntry again");
            } else {
                offer = maybeOffer.get();
                type = TypeItem.getItemFrom(offer.getDescription());
            }
        }
        if (!exit) {
            Optional<Item> maybeItem = session.getImpl().getItemRepository().findByFarmIdAndType(farm.getId(), type);
            if (maybeItem.isEmpty()) {
                return new FarmerMenuMessage("there is not item " + type + " on this farm");
            } else {
                Item item = maybeItem.get();
                log.info("you can sell " + item);
                log.info("you want sell this item? \n[Y]\n[N]\n[0] if you wand exit");
                boolean getAnswer = false;
                while (!exit && !getAnswer) {
                    String requestForMenu = session.getRequestForMenu();
                    if (requestForMenu.equals("0")) {
                        exit = true;
                        return new FarmerMenuMessage("back to user menu");
                    }
                    switch (requestForMenu) {
                        case "Y" -> {
                            Double farmBudget = farm.getBudget();
                            Double income = item.getCount() * offer.getPrice();
                            farm.setBudget(farmBudget + income);
                            session.getImpl().getFarmRepository().update(farm);
                            session.getImpl().getItemRepository().delete(item.getId());
                            getAnswer = true;
                            return new FarmerMenuMessage("was sold " + type + " on " + income);
                        }
                        case "N" -> {
                            getAnswer = true;
                            return new FarmerMenuMessage("back to user menu");
                        }
                        default -> log.info("not correct data");
                    }
                }
            }
        }
        return new FarmerMenuMessage("something go wrong");
    }

    @Override
    public ISessionInfo setSession(Session session) {
        this.session = session;
        return new FarmerSessionInfo(this.session);
    }
}
