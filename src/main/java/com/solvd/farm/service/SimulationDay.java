package com.solvd.farm.service;

import com.solvd.farm.domain.Animal;
import com.solvd.farm.domain.Feed;
import com.solvd.farm.domain.Item;
import com.solvd.farm.domain.enums.TypeItem;
import com.solvd.farm.persistence.AnimalRepository;
import com.solvd.farm.persistence.FeedRepository;
import com.solvd.farm.persistence.ItemRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class SimulationDay extends Thread {

    private static final SimulationDay INSTANCE = new SimulationDay();
    private static final Integer DAY_INTERVAL = 100000;

    private volatile boolean applicationRunning = true;
    @Setter
    private AnimalRepository animalRepository;
    @Setter
    private FeedRepository feedRepository;
    @Setter
    private ItemRepository itemRepository;

    public SimulationDay() {
    }

    @Override
    public void run() {
        while (applicationRunning) {
            try {
                log.info("\n----------------DAY PASS-----------");
                farmProcess();
                sleep(DAY_INTERVAL);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void farmProcess() {
        for (Animal animal : animalRepository.findAll()) {
            boolean animalEat = false;
            for (Feed feed : feedRepository.findAll()) {
                if (feed.getFarm().equals(animal.getFarm()) && feed.getType().name().contains(animal.getType().name())) {
                    Double count = feed.getCount();
                    count -= 0.5;
                    if (count <= 0) {
                        feedRepository.delete(feed.getId());
                    } else {
                        feed.setCount(count);
                        feedRepository.update(feed);
                    }
                    log.info(animal + "\ngrowing");
                    animal.setWeight(animal.getWeight() + 0.1);
                    animalRepository.update(animal);

                    switch (animal.getType()) {
                        case COW -> {

                            log.info("\ngot 3.5 milk");
                            Optional<Item> maybeItem = itemRepository.findByFarmIdAndType(animal.getFarm().getId(), TypeItem.MILK);
                            if (maybeItem.isPresent()) {
                                Item item = maybeItem.get();
                                item.setCount(item.getCount() + 3.5);
                                itemRepository.update(item);
                            } else {
                                itemRepository.save(new Item(null, TypeItem.MILK, 3.5, animal.getFarm()));
                            }
                        }
                        case CHICKEN -> {
                            log.info("\ngot 1 egg");
                            Optional<Item> maybeItem = itemRepository.findByFarmIdAndType(animal.getFarm().getId(), TypeItem.EGG);
                            if (maybeItem.isPresent()) {
                                Item item = maybeItem.get();
                                item.setCount(item.getCount() + 1);
                                itemRepository.update(item);
                            } else {
                                itemRepository.save(new Item(null, TypeItem.EGG, 1.0, animal.getFarm()));
                            }
                        }
                    }
                    animalEat = true;
                    break;
                }
            }
            if (!animalEat) {
                log.info(animal + "\n--------------- DEAD --------------- ");
                animalRepository.delete(animal.getId());
            }
        }
    }

    public static SimulationDay getINSTANCE(TypeIMPL impl) {
        INSTANCE.setAnimalRepository(impl.getAnimalRepository());
        INSTANCE.setFeedRepository(impl.getFeedRepository());
        INSTANCE.setItemRepository(impl.getItemRepository());
        return INSTANCE;
    }

    public boolean stopThread() {
        setApplicationRunning(false);
        return !applicationRunning;
    }

    public void setApplicationRunning(boolean applicationRunning) {
        this.applicationRunning = applicationRunning;
    }
}
