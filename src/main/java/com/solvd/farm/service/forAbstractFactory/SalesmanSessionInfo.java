package com.solvd.farm.service.forAbstractFactory;

import com.solvd.farm.service.Session;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SalesmanSessionInfo implements ISessionInfo {

    public SalesmanSessionInfo(Session session) {
        log.info("Salesman Session Info " + session);
    }
}
