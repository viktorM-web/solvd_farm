package com.solvd.farm.service.forAbstractFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SalesmanMenuMessage implements IMenuMessage {

    public SalesmanMenuMessage(String message) {
        log.info("Salesman Menu Message " + message);
    }
}