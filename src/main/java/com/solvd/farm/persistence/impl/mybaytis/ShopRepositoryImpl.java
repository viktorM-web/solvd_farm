package com.solvd.farm.persistence.impl.mybaytis;

import com.solvd.farm.domain.Shop;
import com.solvd.farm.persistence.ShopRepository;
import com.solvd.farm.util.MyBatisSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class ShopRepositoryImpl implements ShopRepository {

    private static final ShopRepositoryImpl INSTANCE = new ShopRepositoryImpl();

    public static ShopRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ShopRepository mapper = session.getMapper(ShopRepository.class);
            return mapper.delete(id);
        }
    }

    @Override
    public void save(Shop shop) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ShopRepository mapper = session.getMapper(ShopRepository.class);
            mapper.save(shop);
        }
    }

    @Override
    public void update(Shop shop) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ShopRepository mapper = session.getMapper(ShopRepository.class);
            mapper.update(shop);
        }
    }

    @Override
    public Optional<Shop> findById(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ShopRepository mapper = session.getMapper(ShopRepository.class);
            return mapper.findById(id);
        }
    }

    @Override
    public List<Shop> findAll() {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            ShopRepository mapper = session.getMapper(ShopRepository.class);
            return mapper.findAll();
        }
    }
}
