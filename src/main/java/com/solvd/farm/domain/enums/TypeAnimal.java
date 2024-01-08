package com.solvd.farm.domain.enums;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum TypeAnimal {

    CHICKEN,
    PIG,
    COW;

    public static TypeAnimal getItemFrom(String description) {
        for (TypeAnimal typeAnimal : TypeAnimal.values()) {
            if (description.contains(typeAnimal.name())) {
                return typeAnimal;
            }
        }
        log.info("not found name item in description");
        return null;
    }
}
