package com.solvd.farm.service.menu.farmer;

import com.solvd.farm.domain.Farm;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.menu.IMenu;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ShowingFarmMenu implements IMenu {

    @Setter
    private Session session;

    @Override
    public void execute() {
        List<Farm> allFarms = session.getImpl().getFarmRepository().findAll();
        for (Farm farm : allFarms) {
            if (farm.getUser().getId() == session.getUser().getId())
                log.info(farm.getName() + farm.getBudget() + "[" + farm.getId() + "]");
        }
        log.info("back to user menu");
    }

}
