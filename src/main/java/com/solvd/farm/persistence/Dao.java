package com.solvd.farm.persistence;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {

    boolean delete(K id);

    void save(E entity);

    void update(E entity);

    Optional<E> findById(K id);

    List<E> findAll();
}
