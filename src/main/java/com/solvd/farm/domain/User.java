package com.solvd.farm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solvd.farm.domain.enums.Role;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "user")
public class User {

    @XmlAttribute(name = "id")
    private Long id;
    @JsonProperty("log")
    private String login;
    private String password;
    private Role role;

}
