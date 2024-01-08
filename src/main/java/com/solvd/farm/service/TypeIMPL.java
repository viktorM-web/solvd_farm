package com.solvd.farm.service;

import com.solvd.farm.persistence.*;
import com.solvd.farm.persistence.impl.jdbc.*;
import lombok.Getter;
@Getter
public enum TypeIMPL {

    JDBC (AnimalRepositoryImpl.getInstance(),
            FarmRepositoryImpl.getInstance(),
            FeedRepositoryImpl.getInstance(),
            ItemRepositoryImpl.getInstance(),
            OfferRepositoryImpl.getInstance(),
            ShopRepositoryImpl.getInstance(),
            UserRepositoryImpl.getInstance()),

    MY_BATIS(com.solvd.farm.persistence.impl.mybaytis.AnimalRepositoryImpl.getInstance(),
            com.solvd.farm.persistence.impl.mybaytis.FarmRepositoryImpl.getInstance(),
            com.solvd.farm.persistence.impl.mybaytis.FeedRepositoryImpl.getInstance(),
            com.solvd.farm.persistence.impl.mybaytis.ItemRepositoryImpl.getInstance(),
            com.solvd.farm.persistence.impl.mybaytis.OfferRepositoryImpl.getInstance(),
            com.solvd.farm.persistence.impl.mybaytis.ShopRepositoryImpl.getInstance(),
            com.solvd.farm.persistence.impl.mybaytis.UserRepositoryImpl.getInstance());

    private AnimalRepository animalRepository;
    private FarmRepository farmRepository;
    private FeedRepository feedRepository;
    private ItemRepository itemRepository;
    private OfferRepository offerRepository;
    private ShopRepository shopRepository;
    private UserRepository userRepository;

    TypeIMPL(AnimalRepository animalRepository, FarmRepository farmRepository,
             FeedRepository feedRepository, ItemRepository itemRepository,
             OfferRepository offerRepository, ShopRepository shopRepository,
             UserRepository userRepository) {
        this.animalRepository=animalRepository;
        this.farmRepository=farmRepository;
        this.feedRepository=feedRepository;
        this.itemRepository=itemRepository;
        this.offerRepository=offerRepository;
        this.shopRepository=shopRepository;
        this.userRepository=userRepository;
    }

    public AnimalRepository getAnimalRepository() {
        return animalRepository;
    }

    public FarmRepository getFarmRepository() {
        return farmRepository;
    }

    public FeedRepository getFeedRepository() {
        return feedRepository;
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public OfferRepository getOfferRepository() {
        return offerRepository;
    }

    public ShopRepository getShopRepository() {
        return shopRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
