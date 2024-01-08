package com.solvd.farm.service;

import com.solvd.farm.service.menu.*;
import com.solvd.farm.service.menu.admin.CreatingShopMenu;
import com.solvd.farm.service.menu.admin.CreatingUserMenu;
import com.solvd.farm.service.menu.admin.StopAppMenu;
import com.solvd.farm.service.menu.farmer.*;
import com.solvd.farm.service.menu.salesman.CreatingOfferMenu;
import com.solvd.farm.service.menu.salesman.DeletingOfferMenu;
import com.solvd.farm.service.menu.salesman.ShowingOfferMenu;
import com.solvd.farm.service.menu.salesman.UpdatingOfferMenu;

public enum TypeAction {

    BUY_FEED("buy feed", new BuyingFeedMenu()),
    SELL_ITEM("sell item", new SellingItemMenu()),
    SELL_ANIMAL("sell animal", new SellingAnimalMenu()),
    SHOW_ALL_ANIMALS("show all animals", new ShowingAnimalMenu()),
    BUY_ANIMAL("buy animal", new BuyingAnimalMenu()),
    KILL_ANIMAL("kill animal", new KillingAnimalMenu()),
    SHOW_ALL_FARMS("show all farms", new ShowingFarmMenu()),
    CREATE_OFFER("create offer", new CreatingOfferMenu()),
    UPDATE_OFFER("update offer", new UpdatingOfferMenu()),
    DELETE_OFFER("delete offer", new DeletingOfferMenu()),
    SHOW_ALL_OFFER("show all offer", new ShowingOfferMenu()),
    CREATE_USER("create user", new CreatingUserMenu()),
    CREATE_SHOP("create shop", new CreatingShopMenu()),
    STOP_APP("stop app", new StopAppMenu());

    String name;
    IMenu menu;

    TypeAction(String name, IMenu menu) {
        this.name = name;
        this.menu = menu;
    }

    public static IMenu getMenuBy(String name) {
        for (TypeAction type : TypeAction.values()) {
            if (type.name.equals(name)) {
                return type.menu;
            }
        }
        return null;
    }
}
