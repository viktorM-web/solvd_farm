package com.solvd.farm.persistence.impl.jdbc;

import com.solvd.farm.domain.Item;
import com.solvd.farm.domain.enums.TypeItem;
import com.solvd.farm.exception.DaoException;
import com.solvd.farm.persistence.ItemRepository;
import com.solvd.farm.util.ConnectionPool;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private static final ItemRepositoryImpl INSTANCE = new ItemRepositoryImpl();
    private static final FarmRepositoryImpl FARM_REPOSITORY = FarmRepositoryImpl.getInstance();

    private static final String DELETE_SQL = """
            DELETE FROM items
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO items (type, count, farm_id)
            VALUES (?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE items
            SET type = ?,
                count = ?
                farm_id = ?
            WHERE id=?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT type,
                count,
                farm_id
            FROM items
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE items.id=?
            """;

    private ItemRepositoryImpl() {
    }

    public static ItemRepositoryImpl getInstance() {
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
    public void save(Item item) {
        getSaved(item);
    }

    public Item getSaved(Item item) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, item.getType().name());
            preparedStatement.setDouble(2, item.getCount());
            preparedStatement.setLong(3, item.getFarm().getId());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong("GENERATED_KEY");
                item.setId(id);
            }
            return item;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Item item) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, item.getType().name());
            preparedStatement.setDouble(2, item.getCount());
            preparedStatement.setLong(3, item.getFarm().getId());
            preparedStatement.setLong(4, item.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Item> findById(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Item item = null;
            if (resultSet.next()) {
                item = buildItem(resultSet);
            }
            return Optional.ofNullable(item);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<Item> findAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Item> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildItem(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    private static Item buildItem(ResultSet resultSet) throws SQLException {
        return new Item(
                resultSet.getLong("id"),
                TypeItem.valueOf(resultSet.getString("type")),
                resultSet.getDouble("count"),
                FARM_REPOSITORY.findById(
                        resultSet.getLong("farm_id"),
                        resultSet.getStatement().getConnection()
                ).orElse(null)
        );
    }
}
