package com.solvd.farm.persistence.impl.jdbc;

import com.solvd.farm.domain.Feed;
import com.solvd.farm.domain.Item;
import com.solvd.farm.domain.enums.TypeFeed;
import com.solvd.farm.domain.enums.TypeItem;
import com.solvd.farm.exception.DaoException;
import com.solvd.farm.persistence.FeedRepository;
import com.solvd.farm.util.ConnectionPool;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class FeedRepositoryImpl implements FeedRepository {

    private static final FeedRepositoryImpl INSTANCE = new FeedRepositoryImpl();
    private static final FarmRepositoryImpl FARM_REPOSITORY = FarmRepositoryImpl.getInstance();

    private static final String DELETE_SQL = """
            DELETE FROM feeds
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO feeds (type, count, farm_id)
            VALUES (?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE feeds
            SET type = ?,
                count = ?,
                farm_id = ?
            WHERE id=?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
                id,
                type,
                count,
                farm_id
            FROM feeds
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE feeds.id=?
            """;
    private static final String FIND_BY_FARM_ID_AND_TYPE = FIND_ALL_SQL + """
            WHERE farm_id = ? AND type = ? 
            """;

    private FeedRepositoryImpl() {
    }

    public static FeedRepositoryImpl getInstance() {
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
    public void save(Feed feed) {
        getSaved(feed);
    }

    public Feed getSaved(Feed feed) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, feed.getType().name());
            preparedStatement.setDouble(2, feed.getCount());
            preparedStatement.setLong(3, feed.getFarm().getId());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong("GENERATED_KEY");
                feed.setId(id);
            }
            return feed;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Feed feed) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, feed.getType().name());
            preparedStatement.setDouble(2, feed.getCount());
            preparedStatement.setLong(3, feed.getFarm().getId());
            preparedStatement.setLong(4, feed.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Feed> findById(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Feed feed = null;
            if (resultSet.next()) {
                feed = buildFeed(resultSet);
            }
            return Optional.ofNullable(feed);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<Feed> findAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Feed> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildFeed(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Feed> findByFarmIdAndType(Long id, TypeFeed type) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_FARM_ID_AND_TYPE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, type.name());
            var resultSet = preparedStatement.executeQuery();
            Feed feed = null;
            if (resultSet.next()) {
                feed = buildFeed(resultSet);
            }
            return Optional.ofNullable(feed);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    private static Feed buildFeed(ResultSet resultSet) throws SQLException {
        return new Feed(
                resultSet.getLong("id"),
                TypeFeed.valueOf(resultSet.getString("type")),
                resultSet.getDouble("count"),
                FARM_REPOSITORY.findById(
                        resultSet.getLong("farm_id"),
                        resultSet.getStatement().getConnection()
                ).orElse(null)
        );
    }
}
