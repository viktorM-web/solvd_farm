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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class SellingAnimalMenu implements IMenu {

    @Setter
    private Session session;

    private void showAllOffers() {
        List<Offer> allSellingFeed = session.getImpl().getOfferRepository().findAllSellingAnimal();
        for (Offer offer : allSellingFeed) {
            log.info(offer.getDescription() + offer.getPrice() + offer.getShop().getName() + "[" + offer.getId() + "]");
        }
    }

    private Map<Long, Animal> showAllAnimals(Farm farm, TypeAnimal typeAnimal) {
        List<Animal> allSellingAnimals = session.getImpl().getAnimalRepository().findByFarmIdAndType(farm.getId(), typeAnimal);
        for (Animal animal : allSellingAnimals) {
            log.info(animal.getType() + " " + animal.getWeight() + "[" + animal.getId() + "]");
        }
        return allSellingAnimals.stream().collect(Collectors.toMap(Animal::getId, it -> it));
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
            log.info("Enter offer's id which you want to use \nor [0] if you want back to user menu");
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
        Animal animal = null;
        while (!exit && animal == null) {
            Map<Long, Animal> animals = showAllAnimals(farm, type);
            log.info("Enter animal's id which you want to sell \nor [0] if you want back to user menu");
            String requestForMenu = session.getRequestForMenu();
            if (requestForMenu.equals("0")) {
                exit = true;
                break;
            }
            animal = animals.getOrDefault(Long.valueOf(requestForMenu), null);
            if (animal == null) {
                log.info("animal not found \ntry again");
            } else {
                farm.setBudget(farm.getBudget() + offer.getPrice());
                session.getImpl().getFarmRepository().update(farm);
                session.getImpl().getAnimalRepository().delete(animal.getId());
                log.info("was sold " + animal + " for " + offer.getPrice());
            }
        }
    }
}
