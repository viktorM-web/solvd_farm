package com.solvd.farm.persistence.impl.jdbc;

import com.solvd.farm.domain.Shop;
import com.solvd.farm.exception.DaoException;
import com.solvd.farm.persistence.ShopRepository;
import com.solvd.farm.util.ConnectionPool;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ShopRepositoryImpl implements ShopRepository {

    private static final ShopRepositoryImpl INSTANCE = new ShopRepositoryImpl();
    private static final UserRepositoryImpl USER_REPOSITORY = UserRepositoryImpl.getInstance();

    private static final String DELETE_SQL = """
            DELETE FROM shops
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO shops (name, user_id)
            VALUES (?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE shops
            SET name = ?,
                user_id = ?
            WHERE id=?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                name,
                user_id
            FROM shops
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE shops.id=?
            """;

    private ShopRepositoryImpl() {
    }

    public static ShopRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void save(Shop shop) {
        getSaved(shop);
    }

    public Shop getSaved(Shop shop) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, shop.getName());
            preparedStatement.setLong(2, shop.getUser().getId());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong("GENERATED_KEY");
                shop.setId(id);
            }
            return shop;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Shop shop) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, shop.getName());
            preparedStatement.setLong(2, shop.getUser().getId());
            preparedStatement.setLong(3, shop.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Shop> findById(Long id) {
        try (var connection = ConnectionPool.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    public Optional<Shop> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Shop shop = null;
            if (resultSet.next()) {
                shop = buildShop(resultSet);
            }
            return Optional.ofNullable(shop);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<Shop> findAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Shop> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildShop(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    private static Shop buildShop(ResultSet resultSet) throws SQLException {
        return new Shop(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                USER_REPOSITORY.findById(
                        resultSet.getLong("user_id"),
                        resultSet.getStatement().getConnection()
                ).orElse(null)
        );
    }
}
