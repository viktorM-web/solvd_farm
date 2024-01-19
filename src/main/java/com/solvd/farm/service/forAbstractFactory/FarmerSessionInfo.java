package com.solvd.farm.service.forAbstractFactory;

import com.solvd.farm.service.Session;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FarmerSessionInfo implements ISessionInfo {

    public FarmerSessionInfo(Session session) {
        log.info("Farmer Session Info " + session);
    }
}
