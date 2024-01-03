package com.solvd.farm.domain.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public enum Role {

    ADMIN(Map.of(1, "create user", 2, "create shop", 3, "create farm", 4, "create animal",
            5, "create feed", 6,"create item",7,"stop app")),

    SALESMAN(Map.of(1, "create offer", 2, "update offer", 3, "delete offer", 4, "show all offer",
            5, "get all shops")),

    FARMER(Map.of(1, "buy feed", 2, "sell item", 3, "sell animal", 4, "show all animals",
            5, "buy animal", 6, "kill animal", 7, "show all farms"));

    @Getter
    Map<Integer, String> actions;

    Role(Map<Integer, String> map) {
        actions=map;
    }

    public void displayMenu(){
        for (Map.Entry<Integer, String> entry:actions.entrySet()) {
            log.info(entry.getValue()+"[" + entry.getKey() + "]");
        }
    }
}
