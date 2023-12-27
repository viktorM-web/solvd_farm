package com.solvd.farm;

import com.solvd.farm.domain.*;
import com.solvd.farm.domain.enums.*;
import com.solvd.farm.persistence.FarmRepository;
import com.solvd.farm.persistence.ShopRepository;
import com.solvd.farm.persistence.UserRepository;
import com.solvd.farm.persistence.impl.mybaytis.*;
import com.solvd.farm.util.MyBatisSessionFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class Runner {
    public static void main(String[] args) {
        AnimalRepositoryImpl animalRepository = new AnimalRepositoryImpl();
        FeedRepositoryImpl feedRepository = new FeedRepositoryImpl();
//        FarmRepositoryImpl farmRepository = new FarmRepositoryImpl();
//        Farm farm = farmRepository.findById(1L).get();
//
//        Animal animal = animalRepository.findById(79L).get();
//        animal.setWeight(555.6);
//        Feed feed = feedRepository.findById(7L).get();
//        feed.setCount(555.555);

        feedRepository.delete(7L);
        animalRepository.delete(79L);

//        System.out.println(feed);
//        System.out.println(animal);
//
//        System.out.println(animalRepository.findAll());
//        System.out.println(feedRepository.findAll());


//        Item item = new Item(null, TypeItem.EGG, 11.11, new FarmRepositoryImpl().findById(1L).get());

//        ItemRepositoryImpl itemRepository = new ItemRepositoryImpl();
//        itemRepository.save(item);
//        Optional<Item> byId = itemRepository.findById(7L);
//        Item item1 = byId.get();
//        System.out.println(item1);
//        item1.setCount(12.55);
//        itemRepository.delete(7L);
//        System.out.println(itemRepository.findAll());
//        Optional<Item> byId = itemRepository.findById(1L);
//        System.out.println(byId.get());
//        System.out.println(itemRepository.findAll());
//        UserRepositoryImpl repository = new UserRepositoryImpl();
//        User user = new User(null, "test", "test", Role.ADMIN);
//        repository.save(user);
//
//        boolean result = repository.delete(12L);
////        List<User> result = repository.findAll();
//        System.out.println(user);
//        FarmRepositoryImpl repository = new FarmRepositoryImpl();
//        UserRepositoryImpl repository1 = new UserRepositoryImpl();
//        OfferRepositoryImpl offerRepository = new OfferRepositoryImpl();
//        ShopRepositoryImpl shopRepository = new ShopRepositoryImpl();
//        Optional<User> byId = repository1.findById(2L);
//        Farm test = new Farm(null, "Test", 20000.22, byId.get());

//        Shop test = new Shop(null, "test", byId.get());
//       repository.delete(3L);
//        Farm farm = byId1.get();
//        farm.setBudget(500000.55);
//        repository.update(farm);
//        System.out.println(farm);
//        Offer test = offerRepository.findById(10L).get();
//        System.out.println(test);
//        test.setDescription("DEL");
//        offerRepository.delete(10L);
//        System.out.println(test);
//        System.out.println(offerRepository.findById(1L));
//        System.out.println(offerRepository.findAll());

    }
}
