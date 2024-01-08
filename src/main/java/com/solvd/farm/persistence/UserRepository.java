package com.solvd.farm.persistence;

import com.solvd.farm.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

public interface UserRepository extends Dao<Long, User> {

    Optional<User> findBy(@Param("login") String login, @Param("password") String password);
}
