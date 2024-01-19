package com.solvd.farm.service.menu.admin;

import com.solvd.farm.service.Application;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.forAbstractFactory.AdminMenuMessage;
import com.solvd.farm.service.forAbstractFactory.AdminSessionInfo;
import com.solvd.farm.service.forAbstractFactory.IMenuMessage;
import com.solvd.farm.service.forAbstractFactory.ISessionInfo;
import com.solvd.farm.service.menu.IMenu;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class StopAppMenu implements IMenu {

    private Session session;

    @Override
    public IMenuMessage execute() {
        String message = null;
        log.info("you really want to stop app? \n[Y]\n[N]");
        boolean getAnswer = false;
        while (!getAnswer) {
            String requestForMenu = session.getRequestForMenu();
            switch (requestForMenu) {
                case "Y" -> {
                    try {
                        Field applicationWasStop = Application.class.getDeclaredField("applicationWasStop");
                        applicationWasStop.setAccessible(true);
                        applicationWasStop.set(Application.getINSTANCE(), true);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        log.error(e.getMessage());
                    }
                    getAnswer = true;
                    message = "ADMIN STOPPED THE APP";
                }
                case "N" -> {
                    message ="back to user menu";
                    getAnswer = true;
                }
                default -> log.info("not correct data");
            }
        }
        return new AdminMenuMessage(message);
    }

    @Override
    public ISessionInfo setSession(Session session) {
        this.session = session;
        return new AdminSessionInfo(this.session);
    }
}
