package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.TypeOffer;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "offer")
public class Offer {

    @XmlAttribute(name = "id")
    private Long id;
    private TypeOffer type;
    private String description;
    private Double price;
    private Shop shop;

}
