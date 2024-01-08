package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.TypeFeed;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "feed")
public class Feed {

    @XmlAttribute(name = "id")
    private Long id;
    private TypeFeed type;
    private Double count;
    private Farm farm;

}
