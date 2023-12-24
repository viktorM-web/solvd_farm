package com.solvd.farm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Shop {

    private Long id;
    private String name;
    private User user;

}
