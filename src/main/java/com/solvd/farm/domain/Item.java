package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.TypeItem;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class Item {

    @XmlAttribute(name = "id")
    private Long id;
    private TypeItem type;
    private Double count;
    private Farm farm;

}
