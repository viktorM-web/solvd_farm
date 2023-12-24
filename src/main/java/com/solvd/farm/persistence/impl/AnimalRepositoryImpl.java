package com.solvd.farm.persistence.impl;

import com.solvd.farm.domain.Animal;
import com.solvd.farm.domain.enums.TypeAnimal;
import com.solvd.farm.exception.DaoException;
import com.solvd.farm.persistence.AnimalRepository;
import com.solvd.farm.util.ConnectionPool;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
public class AnimalRepositoryImpl implements AnimalRepository {

    private static final AnimalRepositoryImpl INSTANCE = new AnimalRepositoryImpl();
    private static final FarmRepositoryImpl FARM_REPOSITORY = FarmRepositoryImpl.getInstance();

    private static final String DELETE_SQL = """
            DELETE FROM animals
            WHERE animal.id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO animals (type, age, weight, farm_id)
            VALUES (?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE animals
            SET type = ?,
                age = ?,
                weight = ?,
                farm_id = ?
            WHERE id=?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
                id,
                type,
                age,
                weight,
                farm_id
            FROM animals
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE animals.id=?
            """;

    private AnimalRepositoryImpl() {
    }

    public static AnimalRepositoryImpl getInstance() {
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
    public Animal save(Animal animal) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, animal.getType().name());
            preparedStatement.setInt(2, animal.getAge());
            preparedStatement.setDouble(3, animal.getWeight());
            preparedStatement.setLong(4, animal.getFarm().getId());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong("GENERATED_KEY");
                animal.setId(id);
            }
            return animal;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Animal animal) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, animal.getType().name());
            preparedStatement.setInt(2, animal.getAge());
            preparedStatement.setDouble(3, animal.getWeight());
            preparedStatement.setLong(4, animal.getFarm().getId());
            preparedStatement.setLong(5, animal.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Animal> findById(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Animal animal = null;
            if (resultSet.next()) {
                animal = buildAnimal(resultSet);
            }
            return Optional.ofNullable(animal);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<Animal> findAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Animal> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildAnimal(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    private static Animal buildAnimal(ResultSet resultSet) throws SQLException {
        return new Animal(
                resultSet.getLong("id"),
                TypeAnimal.valueOf(resultSet.getString("type")),
                resultSet.getInt("age"),
                resultSet.getDouble("weight"),
                FARM_REPOSITORY.findById(
                        resultSet.getLong("farm_id"),
                        resultSet.getStatement().getConnection()
                ).orElse(null)
        );
    }
}
