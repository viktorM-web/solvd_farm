package com.solvd.farm.persistence.impl.mybaytis;

import com.solvd.farm.domain.Animal;
import com.solvd.farm.persistence.AnimalRepository;
import com.solvd.farm.util.MyBatisSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class AnimalRepositoryImpl implements AnimalRepository {

    @Override
    public boolean delete(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            AnimalRepository mapper = session.getMapper(AnimalRepository.class);
            return mapper.delete(id);
        }
    }

    @Override
    public void save(Animal animal) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            AnimalRepository mapper = session.getMapper(AnimalRepository.class);
            mapper.save(animal);
        }
    }

    @Override
    public void update(Animal animal) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            AnimalRepository mapper = session.getMapper(AnimalRepository.class);
            mapper.update(animal);
        }
    }

    @Override
    public Optional<Animal> findById(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            AnimalRepository mapper = session.getMapper(AnimalRepository.class);
            return mapper.findById(id);
        }
    }

    @Override
    public List<Animal> findAll() {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            AnimalRepository mapper = session.getMapper(AnimalRepository.class);
            return mapper.findAll();
        }
    }
}
