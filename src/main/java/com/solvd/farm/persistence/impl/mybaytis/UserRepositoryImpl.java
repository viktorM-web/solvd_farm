package com.solvd.farm.persistence.impl.mybaytis;

import com.solvd.farm.domain.User;
import com.solvd.farm.persistence.UserRepository;
import com.solvd.farm.util.MyBatisSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public boolean delete(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            UserRepository mapper = session.getMapper(UserRepository.class);
            return mapper.delete(id);
        }
    }

    @Override
    public void save(User user) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            UserRepository mapper = session.getMapper(UserRepository.class);
            mapper.save(user);
        }
    }

    @Override
    public void update(User user) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            UserRepository mapper = session.getMapper(UserRepository.class);
            mapper.update(user);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            UserRepository mapper = session.getMapper(UserRepository.class);
            return mapper.findById(id);
        }
    }

    @Override
    public List<User> findAll() {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            UserRepository mapper = session.getMapper(UserRepository.class);
            return mapper.findAll();
        }
    }
}
