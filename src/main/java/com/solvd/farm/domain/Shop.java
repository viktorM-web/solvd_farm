package com.solvd.farm.domain;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "shop")
public class Shop {

    @XmlAttribute(name = "id")
    private Long id;
    private String name;
    private User user;

    @XmlElementWrapper(name="offers")
    @XmlElement(name = "offer")
    private List<Offer> offers;

}
