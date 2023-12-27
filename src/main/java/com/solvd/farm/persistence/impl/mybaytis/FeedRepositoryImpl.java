package com.solvd.farm.persistence.impl.mybaytis;

import com.solvd.farm.domain.Feed;
import com.solvd.farm.persistence.FeedRepository;
import com.solvd.farm.util.MyBatisSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class FeedRepositoryImpl implements FeedRepository {

    @Override
    public boolean delete(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FeedRepository mapper = session.getMapper(FeedRepository.class);
            return mapper.delete(id);
        }
    }

    @Override
    public void save(Feed feed) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FeedRepository mapper = session.getMapper(FeedRepository.class);
            mapper.save(feed);
        }
    }

    @Override
    public void update(Feed feed) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FeedRepository mapper = session.getMapper(FeedRepository.class);
            mapper.update(feed);
        }
    }

    @Override
    public Optional<Feed> findById(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FeedRepository mapper = session.getMapper(FeedRepository.class);
            return mapper.findById(id);
        }
    }

    @Override
    public List<Feed> findAll() {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            FeedRepository mapper = session.getMapper(FeedRepository.class);
            return mapper.findAll();
        }
    }
}
