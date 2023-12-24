package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.TypeItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {

    private Long id;
    private TypeItem type;
    private Double count;
    private Farm farm;

}
