package com.solvd.farm.service.menu;

import com.solvd.farm.service.Session;
import com.solvd.farm.service.forAbstractFactory.IMenuMessage;
import com.solvd.farm.service.forAbstractFactory.ISessionInfo;

public interface IMenu {

    IMenuMessage execute();

    ISessionInfo setSession(Session session);
}
