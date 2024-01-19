package com.solvd.farm.service.forAbstractFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FarmerMenuMessage implements IMenuMessage {

    public FarmerMenuMessage(String message) {
        log.info("Farmer Menu Message " + message);
    }
}