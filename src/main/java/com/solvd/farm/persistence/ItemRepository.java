package com.solvd.farm.persistence;

import com.solvd.farm.domain.Item;
import com.solvd.farm.domain.enums.TypeItem;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

public interface ItemRepository extends Dao<Long, Item> {

    Optional<Item> findByFarmIdAndType(@Param("id") Long id, @Param("type") TypeItem type);
}
