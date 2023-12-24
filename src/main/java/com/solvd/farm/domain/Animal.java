package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.TypeAnimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Animal {

    private Long id;
    private TypeAnimal type;
    private Integer age;
    private Double weight;
    private Farm farm;

}
