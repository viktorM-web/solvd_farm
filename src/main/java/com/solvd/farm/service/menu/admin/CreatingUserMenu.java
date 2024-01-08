package com.solvd.farm.service.menu.admin;

import com.solvd.farm.domain.User;
import com.solvd.farm.service.Session;
import com.solvd.farm.service.menu.IMenu;
import com.solvd.farm.util.DocumentReader;
import com.solvd.farm.util.JAXBParser;
import com.solvd.farm.util.JSONParser;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class CreatingUserMenu implements IMenu {

    @Setter
    private Session session;

    @Override
    public void execute() {
        boolean exit = false;
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
                    log.info("back to user menu");
                }
                case "2" -> {
                    User user = new User();
                    boolean fileRead = false;
                    while (!fileRead) {
                        log.info("enter name xml file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = JAXBParser.getObject(requestForMenu, user);
                        if (maybeUser.isPresent()) {
                            user = (User) maybeUser.get();
                            fileRead = true;
                        }
                    }
                    session.getImpl().getUserRepository().save(user);

                    if (user.getId() != null) {
                        log.info(user + "was created");
                    } else {
                        log.info("failed to create ");
                    }
                    exit = true;
                }
                case "3" -> {
                    User user = new User();
                    boolean fileRead = false;
                    while (!fileRead) {
                        log.info("enter name json file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = JSONParser.getObject(requestForMenu, user);
                        if (maybeUser.isPresent()) {
                            user = (User) maybeUser.get();
                            fileRead = true;
                        }
                    }
                    session.getImpl().getUserRepository().save(user);

                    if (user.getId() != null) {
                        log.info(user + "was created");
                    } else {
                        log.info("failed to create ");
                    }
                    exit = true;
                }
                case "1" -> {
                    User user = new User();
                    boolean fileRed = false;
                    while (!fileRed) {
                        log.info("enter name xml file");
                        requestForMenu = session.getRequestForMenu();
                        Optional<Object> maybeUser = DocumentReader.getDocument(requestForMenu, user);
                        if (maybeUser.isPresent()) {
                            user = (User) maybeUser.get();
                            fileRed = true;
                        }
                    }
                    session.getImpl().getUserRepository().save(user);

                    if (user.getId() != null) {
                        log.info(user + "was created");
                    } else {
                        log.info("failed to create ");
                    }
                    exit = true;
                }
                default -> log.info("not correct data");
            }
        }
    }
}
