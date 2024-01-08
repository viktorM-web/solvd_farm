package com.solvd.farm.service.menu.farmer;

import com.solvd.farm.domain.Animal;
import com.solvd.farm.domain.Farm;
import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.enums.TypeAnimal;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.menu.IMenu;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class BuyingAnimalMenu implements IMenu {

    @Setter
    private Session session;

    private void showAllOffers() {
        List<Offer> allSellingFeed = session.getImpl().getOfferRepository().findAllBuyingAnimal();
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
    public void execute() {
        Farm farm = null;
        boolean exit = false;
        while (!exit && farm == null) {
            showAllFarms();
            log.info("Enter farm's id which you want to manage \nor [0] if you want back to user menu");
            String requestForMenu = session.getRequestForMenu();
            if (requestForMenu.equals("0")) {
                exit = true;
                break;
            }
            Optional<Farm> maybeFarm = session.getImpl().getFarmRepository().findById(Long.valueOf(requestForMenu));
            if (maybeFarm.isEmpty()) {
                log.info("farm not found \ntry again");
            } else {
                farm = maybeFarm.get();
            }
        }
        Offer offer = null;
        TypeAnimal type = null;
        while (!exit && (offer == null || type == null)) {
            showAllOffers();
            log.info("Enter offer's id which you want to buy \nor [0] if you want back to user menu");
            String requestForMenu = session.getRequestForMenu();
            if (requestForMenu.equals("0")) {
                exit = true;
                break;
            }
            Optional<Offer> maybeOffer = session.getImpl().getOfferRepository().findById(Long.valueOf(requestForMenu));
            if (maybeOffer.isEmpty()) {
                log.info("offer not found \ntry again");
            } else {
                offer = maybeOffer.get();
                type = TypeAnimal.getItemFrom(offer.getDescription());
            }
        }
        if (!exit) {
            Double farmBudget = farm.getBudget();
            Long maxCountAnimal = Math.round(farmBudget / offer.getPrice());
            log.info("Enter count of animal you want to buy (you can buy maximum " + maxCountAnimal + ") \n[0] if you want back to user menu ");
            Integer requestForMenu = session.getRequestIntegerForMenu(maxCountAnimal);
            farm.setBudget(farmBudget - farmBudget * offer.getPrice());

            for (int i = 0; requestForMenu > i; i++) {
                Animal animal = null;
                switch (type) {
                    case COW -> {
                        animal = new Animal(null, TypeAnimal.COW, 6, 100.0, farm);
                    }
                    case PIG -> {
                        animal = new Animal(null, TypeAnimal.PIG, 4, 50.0, farm);
                    }
                    case CHICKEN -> {
                        animal = new Animal(null, TypeAnimal.CHICKEN, 2, 1.3, farm);
                    }
                }
                session.getImpl().getAnimalRepository().save(animal);
                log.info("was bought " + animal);
            }
        }
    }
}
