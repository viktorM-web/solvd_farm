package com.solvd.farm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Farm {

    private Long id;
    private String name;
    private Double budget;
    private User user;

}
