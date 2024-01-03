package com.solvd.farm.persistence.impl.jdbc;

import com.solvd.farm.domain.Offer;
import com.solvd.farm.domain.enums.TypeOffer;
import com.solvd.farm.exception.DaoException;
import com.solvd.farm.persistence.OfferRepository;
import com.solvd.farm.util.ConnectionPool;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class OfferRepositoryImpl implements OfferRepository {

    private static final OfferRepositoryImpl INSTANCE = new OfferRepositoryImpl();
    private static final ShopRepositoryImpl SHOP_REPOSITORY = ShopRepositoryImpl.getInstance();

    private static final String DELETE_SQL = """
            DELETE FROM offers
            WHERE id=?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO offers (type, description, price, shop_id)
            VALUES (?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE offers
            SET type = ?,
                description = ?,
                price = ?,
                shop_id = ?
            WHERE id=?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
                id,
                type,
                description,
                price,
                shop_id
            FROM offers
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            WHERE offers.id=?
            """;
    private static final String FIND_SELLING_FEED = FIND_ALL_SQL + """
            WHERE type like 'SELL' and description like '%FOR_%'
            """;
    private static final String FIND_BUYING_ITEM = FIND_ALL_SQL + """
            WHERE type like 'BUY' and description like '%ITEM_%'
            """;
    private static final String FIND_BUYING_ANIMAL = FIND_ALL_SQL + """
            WHERE type like 'BUY' and description like '%ANIMAL_%'
            """;
    private static final String FIND_SELLING_ANIMAL = FIND_ALL_SQL + """
            WHERE type like 'SELL' and description like '%ANIMAL_%'
            """;

    private OfferRepositoryImpl() {
    }

    public static OfferRepositoryImpl getInstance() {
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
    public void save(Offer offer) {
        getSaved(offer);
    }

    public Offer getSaved(Offer offer) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, offer.getType().name());
            preparedStatement.setString(2, offer.getDescription());
            preparedStatement.setDouble(3, offer.getPrice());
            preparedStatement.setLong(4, offer.getShop().getId());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong("GENERATED_KEY");
                offer.setId(id);
            }
            return offer;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public void update(Offer offer) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, offer.getType().name());
            preparedStatement.setString(2, offer.getDescription());
            preparedStatement.setDouble(3, offer.getPrice());
            preparedStatement.setLong(4, offer.getShop().getId());
            preparedStatement.setLong(5, offer.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Offer> findById(Long id) {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            Offer offer = null;
            if (resultSet.next()) {
                offer = buildOffer(resultSet);
            }
            return Optional.ofNullable(offer);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<Offer> findAll() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Offer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildOffer(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    public List<Offer> findAllSellingFeed() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_SELLING_FEED)) {
            var resultSet = preparedStatement.executeQuery();
            List<Offer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildOffer(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<Offer> findAllBuyingItem() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BUYING_ITEM)) {
            var resultSet = preparedStatement.executeQuery();
            List<Offer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildOffer(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<Offer> findAllBuyingAnimal() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_BUYING_ANIMAL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Offer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildOffer(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    @Override
    public List<Offer> findAllSellingAnimal() {
        try (var connection = ConnectionPool.get();
             var preparedStatement = connection.prepareStatement(FIND_SELLING_ANIMAL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Offer> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(buildOffer(resultSet));
            }
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new DaoException(e);
        }
    }

    private static Offer buildOffer(ResultSet resultSet) throws SQLException {
        return new Offer(
                resultSet.getLong("id"),
                TypeOffer.valueOf(resultSet.getString("type")),
                resultSet.getString("description"),
                resultSet.getDouble("price"),
                SHOP_REPOSITORY.findById(
                        resultSet.getLong("shop_id"),
                        resultSet.getStatement().getConnection()
                ).orElse(null)
        );
    }
}
