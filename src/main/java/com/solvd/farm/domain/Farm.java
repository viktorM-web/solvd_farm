package com.solvd.farm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Farm {

    private Long id;
    private String name;
    private Double budget;
    private User user;

}
