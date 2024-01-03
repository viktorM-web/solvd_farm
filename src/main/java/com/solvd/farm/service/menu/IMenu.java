package com.solvd.farm.service.menu;

import com.solvd.farm.service.Session;

public interface IMenu {

    void execute();

    void setSession(Session session);
}
