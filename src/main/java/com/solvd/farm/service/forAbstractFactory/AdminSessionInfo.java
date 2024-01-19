package com.solvd.farm.service.forAbstractFactory;

import com.solvd.farm.service.Session;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminSessionInfo implements ISessionInfo {

    public AdminSessionInfo(Session session) {
        log.info("Admin Session Info " + session);
    }
}
