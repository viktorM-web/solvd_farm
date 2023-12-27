package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.TypeFeed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feed {

    private Long id;
    private TypeFeed type;
    private Double count;
    private Farm farm;

}
