package com.solvd.farm.service;

import com.solvd.farm.domain.User;
import com.solvd.farm.service.menu.IMenu;
import com.solvd.farm.util.ConnectionPool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class Application {

    @Getter
    private static final Application INSTANCE = new Application();

    private Scanner scanner = new Scanner(System.in);
    private TypeIMPL IMPL;
    private SimulationDay simulationDay;
    private Boolean applicationWasStop=false;

    private Application(){
    }

    public void runApplication(){
        selectImplForWorkingWithDatabase();
        runSimulationDay();
        runInteractingMenu();
        shutdownApp();
    }

    private void selectImplForWorkingWithDatabase() {
        log.info("Hello \n how do you want to work with the database\n JDBC press [1]\n MyBatis press [2]");

        while (IMPL == null) {
            switch (scanner.nextLine()) {
                case "1" -> IMPL = TypeIMPL.JDBC;
                case "2" -> IMPL = TypeIMPL.MY_BATIS;
                default -> log.info("not correct data; try again");
            }
        }
    }

    private void runSimulationDay() {
        simulationDay = SimulationDay.getINSTANCE(IMPL);
        simulationDay.start();
    }

    private void runInteractingMenu() {
        while (!applicationWasStop) {
            boolean backToAuthentication = false;
            Session session = new Session(scanner, IMPL);
            log.info("login in system");
            User user = session.authentication();
            while (!backToAuthentication && !applicationWasStop) {
                Optional<IMenu> maybe = session.selectMenu();
                if (maybe.isPresent()) {
                    maybe.get().execute();
                } else {
                    backToAuthentication = true;
                }
            }
        }
    }

    private void shutdownApp() {
        simulationDay.stopThread();
        simulationDay.interrupt();

        scanner.close();
        if (IMPL==TypeIMPL.JDBC){
            ConnectionPool.closePool();
        }
        log.info("APP WAS STOPPED");
    }
}
