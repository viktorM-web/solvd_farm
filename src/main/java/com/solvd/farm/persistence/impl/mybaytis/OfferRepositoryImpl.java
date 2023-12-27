package com.solvd.farm.persistence.impl.mybaytis;

import com.solvd.farm.domain.Offer;
import com.solvd.farm.persistence.OfferRepository;
import com.solvd.farm.util.MyBatisSessionFactory;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class OfferRepositoryImpl implements OfferRepository {

    @Override
    public boolean delete(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            OfferRepository mapper = session.getMapper(OfferRepository.class);
            return mapper.delete(id);
        }
    }

    @Override
    public void save(Offer offer) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            OfferRepository mapper = session.getMapper(OfferRepository.class);
            mapper.save(offer);
        }
    }

    @Override
    public void update(Offer offer) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            OfferRepository mapper = session.getMapper(OfferRepository.class);
            mapper.update(offer);
        }
    }

    @Override
    public Optional<Offer> findById(Long id) {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            OfferRepository mapper = session.getMapper(OfferRepository.class);
            return mapper.findById(id);
        }
    }

    @Override
    public List<Offer> findAll() {
        try (SqlSession session = MyBatisSessionFactory.getSession()) {
            OfferRepository mapper = session.getMapper(OfferRepository.class);
            return mapper.findAll();
        }
    }
}
