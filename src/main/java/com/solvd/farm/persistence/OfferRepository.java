package com.solvd.farm.persistence;

import com.solvd.farm.domain.Offer;

import java.util.List;

public interface OfferRepository extends Dao<Long, Offer> {

    List<Offer> findAllSellingFeed();

    List<Offer> findAllBuyingItem();

    List<Offer> findAllBuyingAnimal();

    List<Offer> findAllSellingAnimal();
}
