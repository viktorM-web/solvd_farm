package com.solvd.farm.persistence.impl.jdbc;

import com.solvd.farm.domain.Farm;
import com.solvd.farm.exception.DaoException;
import com.solvd.farm.persistence.FarmRepository;
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
public class FarmRepositoryImpl implements FarmRepository {

    private static final FarmRepositoryImpl INSTANCE = new FarmRepositoryImpl();
    private static final UserRepositoryImpl USER_REPOSITORY = UserRepositoryImpl.getInstance();

    private static final String DELETE_SQL = """
            DELETE FROM farms
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO farms (name, budget, user_id)
            VALUES (?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE farms
            SET name = ?,
                budget = ?,
                user_id = ?
            WHERE id=?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                name,
                budget,
                user_id
            FROM farms
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE farms.id=?
            """;

    private FarmRepositoryImpl() {
    }

    public static FarmRepositoryImpl getInstance() {
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
    public void save(Farm farm){
        getSaved(farm);
    }

    public Farm getSaved(Farm farm) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, farm.getName());
            preparedStatement.setDouble(2, farm.getBudget());
            preparedStatement.setLong(3, farm.getUser().getId());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong("GENERATED_KEY");
                farm.setId(id);
            }
            return farm;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Farm farm) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, farm.getName());
            preparedStatement.setDouble(2, farm.getBudget());
            preparedStatement.setLong(3, farm.getUser().getId());
            preparedStatement.setLong(4, farm.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Farm> findById(Long id) {
        try (var connection = ConnectionPool.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    public Optional<Farm> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Farm farm = null;
            if (resultSet.next()) {
                farm = buildFarm(resultSet);
            }
            return Optional.ofNullable(farm);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<Farm> findAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Farm> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildFarm(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    private static Farm buildFarm(ResultSet resultSet) throws SQLException {
        return new Farm(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDouble("budget"),
                USER_REPOSITORY.findById(
                        resultSet.getLong("user_id"),
                        resultSet.getStatement().getConnection()
                ).orElse(null)
        );
    }
}
