package com.solvd.farm.domain.enums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum TypeItem {

    EGG,
    MILK,
    MEAT;

    public static TypeItem getItemFrom(String description){
        for (TypeItem typeItem:TypeItem.values()) {
            if(description.contains(typeItem.name())){
                return typeItem;
            }
        }
        log.info("not found name item in description");
        return null;
    }

}
