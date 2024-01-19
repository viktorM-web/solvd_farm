package com.solvd.farm.service.menu.farmer;

import com.solvd.farm.domain.Animal;
import com.solvd.farm.domain.Farm;
import com.solvd.farm.domain.Item;
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
public class KillingAnimalMenu implements IMenu {

    private Session session;

    private void showAllFarms() {
        List<Farm> allFarms = session.getImpl().getFarmRepository().findAll();
        for (Farm farm : allFarms) {
            if (farm.getUser().getId() == session.getUser().getId())
                log.info(farm.getName() + farm.getBudget() + "[" + farm.getId() + "]");
        }
    }

    private void showAllAnimal(Farm farm) {
        List<Animal> allAnimal = session.getImpl().getAnimalRepository().findAllBy(farm.getId());
        for (Animal animal : allAnimal) {
            log.info(animal.getType() + " " + animal.getWeight() + " [" + animal.getId() + "]");
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
        String message = null;
        while (!exit) {
            showAllAnimal(farm);
            log.info("Enter animal's id which you want to kill \nor [0] if you want back to user menu");
            String requestForMenu = session.getRequestForMenu();
            if (requestForMenu.equals("0")) {
                exit = true;
                return new FarmerMenuMessage("back to user menu");
            }
            Optional<Animal> maybeAnimal = session.getImpl().getAnimalRepository().findBy(Long.valueOf(requestForMenu), farm.getId());
            if (maybeAnimal.isEmpty()) {
                log.info("animal not found \ntry again");
            } else {
                Animal animal = maybeAnimal.get();
                session.getImpl().getAnimalRepository().delete(animal.getId());
                Optional<Item> byFarmIdAndType = session.getImpl().getItemRepository().findByFarmIdAndType(farm.getId(), TypeItem.MEAT);
                Item item = null;
                if (byFarmIdAndType.isEmpty()) {
                    item = new Item(null, TypeItem.MEAT, animal.getWeight(), farm);
                    session.getImpl().getItemRepository().save(item);
                } else {
                    item = byFarmIdAndType.get();
                    item.setCount(item.getCount() + animal.getWeight());
                    session.getImpl().getItemRepository().update(item);
                }
                log.info("was add " + animal.getWeight() + " MEAT");
                if (message != null) {
                    message = message.concat("\nwas add " + animal.getWeight() + " MEAT");
                } else {
                    message = "was add " + animal.getWeight() + " MEAT";
                }
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
