package com.solvd.farm.domain;

import com.solvd.farm.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private Long id;
    private String login;
    private String password;
    private Role role;

}
