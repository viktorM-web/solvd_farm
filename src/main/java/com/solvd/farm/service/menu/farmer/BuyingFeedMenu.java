package com.solvd.farm.service.menu.farmer;

import com.solvd.farm.domain.Farm;
import com.solvd.farm.domain.Feed;
import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.enums.TypeFeed;
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
public class BuyingFeedMenu implements IMenu {

    private Session session;

    private void showAllOffers() {
        List<Offer> allSellingFeed = session.getImpl().getOfferRepository().findAllSellingFeed();
        for (Offer offer : allSellingFeed) {
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
        Farm farm = null;
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
        TypeFeed type = null;
        while (!exit && (offer == null || type == null)) {
            showAllOffers();
            log.info("Enter offer's id which you want to buy \nor [0] if you want back to user menu");
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
                type = TypeFeed.getFeedFrom(offer.getDescription());
            }

        }
        String message = null;
        if (!exit) {
            Double farmBudget = farm.getBudget();
            Double price = offer.getPrice();
            if (farmBudget > price) {
                farm.setBudget(farmBudget - price);
                session.getImpl().getFarmRepository().update(farm);

                Optional<Feed> maybeFeed = session.getImpl().getFeedRepository().findByFarmIdAndType(farm.getId(), type);
                if (maybeFeed.isEmpty()) {
                    Feed feed = new Feed(null, type, 10.5, farm);
                    session.getImpl().getFeedRepository().save(feed);
                } else {
                    Feed feed = maybeFeed.get();
                    feed.setCount(feed.getCount() + 10.5);
                    session.getImpl().getFeedRepository().update(feed);
                }
                message = "was bought 10.5 feed" + type;
            } else {
                message = "budget not enough for purchase";
            }
        }
        return new FarmerMenuMessage(message);
    }

    @Override
    public ISessionInfo setSession(Session session) {
        this.session = session;
        return new FarmerSessionInfo(this.session);
    }
}
