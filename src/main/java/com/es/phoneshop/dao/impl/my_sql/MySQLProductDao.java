package com.es.phoneshop.dao.impl.my_sql;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.utils.DBConnector;
import com.es.phoneshop.dao.utils.DBQueries;
import com.es.phoneshop.enums.DBPriceHistoryColumn;
import com.es.phoneshop.enums.DBProductColumn;
import com.es.phoneshop.model.ProductPrice;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.PriceHistoryEntry;
import com.es.phoneshop.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class MySQLProductDao implements ProductDao {
    private final DBConnector connector;
    public static final String ERROR_MESSAGE = "Invalid product id!";

    @Override
    public Optional<Product> findById(Long id) {
        validateId(id);
        return connector.execute(DBQueries.FIND_PRODUCT_BY_ID, preparedStatement -> {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Product product = buildProduct(resultSet);
                fetchPriceHistory(product, resultSet);
                while (resultSet.next()) {
                    fetchPriceHistory(product, resultSet);
                }
                return Optional.of(product);
            }
            return Optional.empty();
        });
    }

    @Override
    public List<Product> findAll() {
        return connector.execute(DBQueries.FIND_ALL_PRODUCTS, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            return buildProducts(resultSet);
        });
    }

    @Override
    public void save(Product product) {
        Optional<ProductPrice> optionalProductPrice = findExistingProductPrice(product.getId());
        Long productId;
        if (optionalProductPrice.isPresent()) {
            connector.execute(DBQueries.UPDATE_PRODUCT, updateProductStatement -> {
                setupProductStatement(updateProductStatement, product);
                updateProductStatement.setLong(7, product.getId());
                updateProductStatement.executeUpdate();
            });
            productId = product.getId();
        } else {
            optionalProductPrice = Optional.of(ProductPrice.builder()
                    .price(product.getPrice())
                    .currency(product.getCurrency())
                    .build());
            productId = saveNewProduct(product);
        }
        updatePriceHistory(optionalProductPrice.get(), productId);
    }

    @Override
    public List<CartItem> findByOrderId(Long orderId) {
        return connector.execute(DBQueries.FIND_PRODUCTS_BY_ORDER_ID, preparedStatement -> {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return buildOrderItems(resultSet);
        });
    }

    private void updatePriceHistory(ProductPrice productPrice, Long productId) {
        connector.execute(DBQueries.UPDATE_PRICE_HISTORY, preparedStatement -> {
            setupPriceHistoryStatement(preparedStatement, productId,
                    productPrice.getPrice(), productPrice.getCurrency().getCurrencyCode());
        });
    }

    private Long saveNewProduct(Product product) {
        connector.execute(DBQueries.SAVE_PRODUCT, saveProductStatement -> {
            setupProductStatement(saveProductStatement, product);
            saveProductStatement.executeUpdate();
        });
        return connector.execute(DBQueries.GET_LAST_INSERTED_PRODUCT_ID, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(DBProductColumn.LAST_INSERTED_ITEM.getValue());
            }
            return -1L;
        });
    }

    private Optional<ProductPrice> findExistingProductPrice(Long id) {
        return connector.execute(DBQueries.FIND_PRODUCT_PRICE_BY_ID, preparedStatement -> {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ProductPrice.builder()
                        .price(resultSet.getBigDecimal(1))
                        .currency(Currency.getInstance(resultSet.getString(2)))
                        .build());
            } else return Optional.empty();
        });
    }

    private void setupPriceHistoryStatement(PreparedStatement preparedStatement, Long id, BigDecimal price,
                                            String currency) throws SQLException {
        int index = 0;
        preparedStatement.setLong(++index, id);
        preparedStatement.setTimestamp(++index, Timestamp.valueOf(LocalDateTime.now()));
        preparedStatement.setBigDecimal(++index, price);
        preparedStatement.setString(++index, currency);
    }

    private void setupProductStatement(PreparedStatement productStatement, Product product)
            throws SQLException {
        int index = 0;
        productStatement.setString(++index, product.getCode());
        productStatement.setString(++index, product.getDescription());
        productStatement.setBigDecimal(++index, product.getPrice());
        productStatement.setString(++index, product.getCurrency().getCurrencyCode());
        productStatement.setInt(++index, product.getStock());
        productStatement.setString(++index, product.getImageUrl());
    }

    private List<CartItem> buildOrderItems(ResultSet resultSet) throws SQLException {
        List<CartItem> cartItems = new ArrayList<>();
        while (resultSet.next()) {
            cartItems.add(new CartItem(buildProduct(resultSet),
                    resultSet.getInt(DBProductColumn.QUANTITY_COLUMN.getValue())));
        }
        return cartItems;
    }

    private List<Product> buildProducts(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Long id = resultSet.getLong(DBProductColumn.ID_COLUMN.getValue());
            Optional<Product> optionalProduct = products.stream()
                    .filter(product -> Objects.equals(product.getId(), id))
                    .findFirst();
            if (optionalProduct.isPresent()) {
                fetchPriceHistory(optionalProduct.get(), resultSet);
            } else {
                products.add(buildProduct(resultSet));
            }
        }
        return products;
    }

    private void fetchPriceHistory(Product product, ResultSet resultSet) throws SQLException {
        product.getPriceHistory().add(PriceHistoryEntry.builder()
                .priceChangeDate(resultSet.getTimestamp(DBPriceHistoryColumn.PRICE_CHANGE_DATE_COLUMN.getValue())
                        .toLocalDateTime())
                .price(resultSet.getBigDecimal(DBPriceHistoryColumn.PRICE_COLUMN.getValue()))
                .currency(Currency.getInstance(
                        resultSet.getString(DBPriceHistoryColumn.CURRENCY_COLUMN.getValue())))
                .build());
    }

    private Product buildProduct(ResultSet resultSet) throws SQLException {
        return Product.builder()
                .id(resultSet.getLong(DBProductColumn.ID_COLUMN.getValue()))
                .code(resultSet.getString(DBProductColumn.CODE_COLUMN.getValue()))
                .description(resultSet.getString(DBProductColumn.DESCRIPTION_COLUMN.getValue()))
                .price(resultSet.getBigDecimal(DBProductColumn.PRICE_COLUMN.getValue()))
                .currency(Currency.getInstance(resultSet.getString(DBProductColumn.CURRENCY_COLUMN.getValue())))
                .stock(resultSet.getInt(DBProductColumn.STOCK_COLUMN.getValue()))
                .imageUrl(resultSet.getString(DBProductColumn.IMAGE_URL_COLUMN.getValue()))
                .build();
    }

    private void validateId(Long id) {
        if (id <= 0) {
            log.error(ERROR_MESSAGE);
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }
}
