package com.solvd.farm.persistence.impl.mybaytis;

import com.solvd.farm.domain.Item;
import com.solvd.farm.persistence.ItemRepository;
import com.solvd.farm.util.MyBatisSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class ItemRepositoryImpl implements ItemRepository {

    @Override
    public boolean delete(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ItemRepository mapper = session.getMapper(ItemRepository.class);
            return mapper.delete(id);
        }
    }

    @Override
    public void save(Item item) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ItemRepository mapper = session.getMapper(ItemRepository.class);
            mapper.save(item);
        }
    }

    @Override
    public void update(Item item) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ItemRepository mapper = session.getMapper(ItemRepository.class);
            mapper.update(item);
        }
    }

    @Override
    public Optional<Item> findById(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ItemRepository mapper = session.getMapper(ItemRepository.class);
            return mapper.findById(id);
        }
    }

    @Override
    public List<Item> findAll() {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ItemRepository mapper = session.getMapper(ItemRepository.class);
            return mapper.findAll();
        }
    }
}
