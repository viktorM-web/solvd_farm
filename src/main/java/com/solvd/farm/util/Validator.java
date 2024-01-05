package com.solvd.farm.util;

import com.solvd.farm.domain.enums.TypeAnimal;
import com.solvd.farm.domain.enums.TypeItem;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Validator {

    public static final String PATTERN_DOUBLE = "[0-9]{1,13}(\\.[0-9]{2})?";
    public static final String PATTERN_DESCRIPTION_FOR_ITEM_TYPE = "ITEM_";
    public static final List<String> PATTERN_DESCRIPTION_FOR_ANIMAL_TYPE = List.of("ANIMAL_", "FOR_");

    public static boolean isCorrectDouble(String string){
        return string.matches(PATTERN_DOUBLE);
    }

    public static boolean isCorrectDescription(String request) {
        for (String pattern:PATTERN_DESCRIPTION_FOR_ANIMAL_TYPE){
            for (TypeAnimal animal: TypeAnimal.values()) {
                String complexPattern = pattern+animal.name();
                if(complexPattern.equals(request)){
                    return true;
                }

            }
        }
        for (TypeItem item :TypeItem.values()) {
            String complexPattern = PATTERN_DESCRIPTION_FOR_ITEM_TYPE+item.name();
            if(complexPattern.equals(request)){
                return true;
            }
        }
        return false;
    }
}
