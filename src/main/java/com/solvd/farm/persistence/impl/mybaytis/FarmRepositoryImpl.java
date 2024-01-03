package com.solvd.farm.persistence.impl.mybaytis;

import com.solvd.farm.domain.Farm;
import com.solvd.farm.persistence.FarmRepository;
import com.solvd.farm.util.MyBatisSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class FarmRepositoryImpl implements FarmRepository {

    private static final FarmRepositoryImpl INSTANCE = new FarmRepositoryImpl();

    public static FarmRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FarmRepository mapper = session.getMapper(FarmRepository.class);
            return mapper.delete(id);
        }
    }

    @Override
    public void save(Farm farm) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FarmRepository mapper = session.getMapper(FarmRepository.class);
            mapper.save(farm);
        }
    }

    @Override
    public void update(Farm farm) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FarmRepository mapper = session.getMapper(FarmRepository.class);
            mapper.update(farm);
        }
    }

    @Override
    public Optional<Farm> findById(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FarmRepository mapper = session.getMapper(FarmRepository.class);
            return mapper.findById(id);
        }
    }

    @Override
    public List<Farm> findAll() {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FarmRepository mapper = session.getMapper(FarmRepository.class);
            return mapper.findAll();
        }
    }
}
