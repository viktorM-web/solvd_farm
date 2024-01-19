package com.solvd.farm.service.menu.farmer;

import com.solvd.farm.domain.Farm;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.forAbstractFactory.FarmerMenuMessage;
import com.solvd.farm.service.forAbstractFactory.FarmerSessionInfo;
import com.solvd.farm.service.forAbstractFactory.IMenuMessage;
import com.solvd.farm.service.forAbstractFactory.ISessionInfo;
import com.solvd.farm.service.menu.IMenu;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ShowingFarmMenu implements IMenu {

    private Session session;

    @Override
    public IMenuMessage execute() {
        List<Farm> allFarms = session.getImpl().getFarmRepository().findAll();
        for (Farm farm : allFarms) {
            if (farm.getUser().getId() == session.getUser().getId())
                log.info(farm.getName() + farm.getBudget() + "[" + farm.getId() + "]");
        }
        return new FarmerMenuMessage("something go wrong");
    }

    @Override
    public ISessionInfo setSession(Session session) {
        this.session = session;
        return new FarmerSessionInfo(this.session);
    }
}
