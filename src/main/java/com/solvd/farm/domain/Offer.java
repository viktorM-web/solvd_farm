package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.TypeOffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {

    private Long id;
    private TypeOffer type;
    private String description;
    private Double price;
    private Shop shop;

}
