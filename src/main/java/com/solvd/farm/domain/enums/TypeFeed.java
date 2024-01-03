package com.solvd.farm.domain.enums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum TypeFeed {

    FOR_CHICKEN,
    FOR_PIG,
    FOR_COW;

    public static TypeFeed getFeedFrom(String description){
        for (TypeFeed typeFeed:TypeFeed.values()) {
            if(description.contains(typeFeed.name())){
                return typeFeed;
            }
        }
        log.info("not found name feed in description");
        return null;
    }
}
