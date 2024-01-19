package com.solvd.farm.service.menu.admin;

import com.solvd.farm.domain.User;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.forAbstractFactory.AdminMenuMessage;
import com.solvd.farm.service.forAbstractFactory.AdminSessionInfo;
import com.solvd.farm.service.forAbstractFactory.IMenuMessage;
import com.solvd.farm.service.forAbstractFactory.ISessionInfo;
import com.solvd.farm.service.menu.IMenu;
import com.solvd.farm.util.Parser;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class CreatingUserMenu implements IMenu {

    private Session session;

    @Override
    public IMenuMessage execute() {
        boolean exit = false;
        String message=null;
        while (!exit) {
            log.info("you want to create user " +
                     "\nby xml press [1]" +
                     "\nby xml(JAXB) press[2] " +
                     "\nby json press[3] " +
                     "\nif you want exit [0]");
            String requestForMenu = session.getRequestForMenu();
            switch (requestForMenu) {
                case "0" -> {
                    exit = true;
                    message = "back to user menu";
                }
                case "2" -> {
                    User user = new User();
                    boolean fileRead = false;
                    while (!fileRead) {
                        log.info("enter name xml file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = Parser.JAXB.parseTo(requestForMenu, user);
                        if (maybeUser.isPresent()) {
                            user = (User) maybeUser.get();
                            fileRead = true;
                        }
                    }
                    session.getImpl().getUserRepository().save(user);

                    if (user.getId() != null) {
                        message = user + "was created";
                    } else {
                        message = "failed to create ";
                    }
                    exit = true;
                }
                case "3" -> {
                    User user = new User();
                    boolean fileRead = false;
                    while (!fileRead) {
                        log.info("enter name json file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = Parser.JSON.parseTo(requestForMenu, user);
                        if (maybeUser.isPresent()) {
                            user = (User) maybeUser.get();
                            fileRead = true;
                        }
                    }
                    session.getImpl().getUserRepository().save(user);

                    if (user.getId() != null) {
                        message = user + "was created";
                    } else {
                        message = "failed to create";
                    }
                    exit = true;
                }
                case "1" -> {
                    User user = new User();
                    boolean fileRed = false;
                    while (!fileRed) {
                        log.info("enter name xml file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = Parser.DOM.parseTo(requestForMenu, user);
                        if (maybeUser.isPresent()) {
                            user = (User) maybeUser.get();
                            fileRed = true;
                        }
                    }
                    session.getImpl().getUserRepository().save(user);

                    if (user.getId() != null) {
                        message = user + "was created";
                    } else {
                        message = "failed to create ";
                    }
                    exit = true;
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
