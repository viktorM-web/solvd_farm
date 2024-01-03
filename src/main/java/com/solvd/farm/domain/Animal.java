package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.TypeAnimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Animal {

    private Long id;
    private TypeAnimal type;
    private Integer age;
    private Double weight;
    private Farm farm;

//    List<Animal> animals;
//    List<Item> items;
//    List<Feed> feeds;

}
