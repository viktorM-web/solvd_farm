package com.solvd.farm.service;

import com.solvd.farm.domain.User;
import com.solvd.farm.service.menu.IMenu;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Scanner;

@Slf4j
@Getter
public class Session {

    private TypeIMPL impl;
    private Scanner scanner;
    private User user;

    public Session(Scanner scanner, TypeIMPL impl) {
        this.scanner = scanner;
        this.impl = impl;
    }

    public User authentication() {
        while (user == null) {
            log.info("Enter login");
            String login = scanner.nextLine();
            log.info("Enter password");
            String password = scanner.nextLine();
            Optional<User> maybeUser = impl.getUserRepository().findBy(login, password);
            if (maybeUser.isEmpty()) {
                log.info("user not found \nnot correct login or password");
            } else {
                user = maybeUser.get();
            }
        }
        return user;
    }

    public Optional<IMenu> selectMenu() {
        displayAvailableMenu();
        log.info("for exit press [0]");
        String select = scanner.nextLine();

        if (select.equals("0")) {
            return Optional.empty();
        }

        String nameAction = user.getRole().getActions().get(Integer.valueOf(select));
        IMenu menu = TypeAction.getMenuBy(nameAction);
        menu.setSession(this);
        return Optional.of(menu);
    }

    public void displayAvailableMenu() {
        user.getRole().displayMenu();
    }

    public String getRequestForMenu() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
    }

    public Integer getRequestIntegerForMenu(Long max) {

        int result=0;

        while (true) {
            String line = scanner.nextLine();
            try {
                result = Integer.parseInt(line);
                if(result>=0||result<=max){
                    return result;
                }
            } catch (NumberFormatException e) {
                log.info("not correct data");
            }
            log.info("not correct number");
        }
    }
}
