package com.solvd.farm.persistence.impl.jdbc;

import com.solvd.farm.domain.User;
import com.solvd.farm.domain.enums.Role;
import com.solvd.farm.exception.DaoException;
import com.solvd.farm.persistence.UserRepository;
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
public class UserRepositoryImpl implements UserRepository {

    private static final UserRepositoryImpl INSTANCE = new UserRepositoryImpl();

    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO users (login, password, role)
            VALUES (?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE users
            SET login = ?,
                password = ?,
                role = ?
            WHERE id=?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                login,
                password,
                role
            FROM users
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE users.id=?
            """;

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
    public void save(User user) {
        getSaved(user);
    }

    public User getSaved(User user) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().name());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong("GENERATED_KEY");
                user.setId(id);
            }
            return user;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void update(User user) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().name());
            preparedStatement.setLong(4, user.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (var connection = ConnectionPool.get()) {
            return findById(id, connection);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    public Optional<User> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<User> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildUser(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }

    private UserRepositoryImpl() {
    }

    public static UserRepositoryImpl getInstance() {
        return INSTANCE;
    }
}
