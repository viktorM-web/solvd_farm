package com.solvd.farm.service.forAbstractFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminMenuMessage implements IMenuMessage {

    public AdminMenuMessage(String message) {
        log.info("Admin Menu Message " + message);
    }
}
