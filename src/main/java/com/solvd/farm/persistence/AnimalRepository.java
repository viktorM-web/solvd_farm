package com.solvd.farm.persistence;

import com.solvd.farm.domain.Animal;
import com.solvd.farm.domain.enums.TypeAnimal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends Dao<Long, Animal> {

    List<Animal> findAllBy(Long farmId);

    Optional<Animal> findBy(@Param("id") Long id, @Param("farmId") Long farmId);
    List<Animal> findByFarmIdAndType(@Param("farmId") Long farmId, @Param("type") TypeAnimal type);
}
