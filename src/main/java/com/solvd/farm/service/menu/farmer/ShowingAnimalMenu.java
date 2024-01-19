package com.solvd.farm.service.menu.farmer;

import com.solvd.farm.domain.Animal;
import com.solvd.farm.domain.Farm;
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
public class ShowingAnimalMenu implements IMenu {

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
        while (farm == null) {
            showAllFarms();
            log.info("Enter farm's id which you want to manage \nor [0] if you want back to user menu");
            String requestForMenu = session.getRequestForMenu();
            if (requestForMenu.equals("0")) {
                return new FarmerMenuMessage("back to user menu");
            }
            Optional<Farm> maybeFarm = session.getImpl().getFarmRepository().findById(Long.valueOf(requestForMenu));
            if (maybeFarm.isEmpty()) {
                log.info("farm not found \ntry again");
            } else {
                farm = maybeFarm.get();
                showAllAnimal(farm);
                log.info("If you want select other farm press [enter] \nor [0] if you want back to user menu");
                requestForMenu = session.getRequestForMenu();
                if (!requestForMenu.equals("0")) {
                    farm = null;
                }
            }
        }
        return new FarmerMenuMessage("back to user menu");
    }

    @Override
    public ISessionInfo setSession(Session session) {
        this.session = session;
        return new FarmerSessionInfo(this.session);
    }
}
