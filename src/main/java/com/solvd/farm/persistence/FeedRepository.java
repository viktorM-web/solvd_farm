package com.solvd.farm.persistence;

import com.solvd.farm.domain.Feed;
import com.solvd.farm.domain.enums.TypeFeed;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

public interface FeedRepository extends Dao<Long, Feed>{

    Optional<Feed> findByFarmIdAndType(@Param("id") Long id, @Param("type") TypeFeed type);
}
