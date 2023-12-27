package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.TypeItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private Long id;
    private TypeItem type;
    private Double count;
    private Farm farm;

}
