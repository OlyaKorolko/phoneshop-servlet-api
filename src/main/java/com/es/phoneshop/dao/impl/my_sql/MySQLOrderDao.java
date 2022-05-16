package com.es.phoneshop.dao.impl.my_sql;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.OrderItemDao;
import com.es.phoneshop.dao.utils.DBConnector;
import com.es.phoneshop.dao.utils.DBQueries;
import com.es.phoneshop.enums.db.DBOrderColumn;
import com.es.phoneshop.enums.PaymentMethod;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class MySQLOrderDao implements OrderDao {
    private final DBConnector connector;
    private static final String ERROR_MESSAGE = "Invalid order id!";
    private final OrderItemDao orderItemDao;

    @Override
    public Optional<Order> findById(Long id) {
        validateId(id);
        return connector.execute(DBQueries.FIND_ORDER_BY_ID, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildOrder(resultSet));
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<Order> findBySecureId(String id) {
        validateSecureId(id);
        return connector.execute(DBQueries.FIND_ORDER_BY_SECURE_ID, preparedStatement -> {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildOrder(resultSet));
            }
            return Optional.empty();
        });
    }

    @Override
    public void save(Order order, List<CartItem> cartItems) {
        if (order != null) {
            connector.execute(DBQueries.SAVE_ORDER, preparedStatement -> {
                setupOrderStatement(preparedStatement, order);
                preparedStatement.executeUpdate();
            });
            orderItemDao.save(cartItems);
        }
    }

    @Override
    public List<Order> findAll(String sessionId) {
        return connector.execute(DBQueries.FIND_ALL_ORDERS, preparedStatement -> {
            preparedStatement.setString(1, sessionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return buildOrders(resultSet);
        });
    }

    private void setupOrderStatement(PreparedStatement productStatement, Order order)
            throws SQLException {
        int index = 0;
        productStatement.setString(++index, order.getSecureId());
        productStatement.setBigDecimal(++index, order.getSubtotalCost());
        productStatement.setBigDecimal(++index, order.getDeliveryCost());
        productStatement.setString(++index, order.getFirstName());
        productStatement.setString(++index, order.getLastName());
        productStatement.setString(++index, order.getPhone());
        productStatement.setTimestamp(++index, Timestamp.valueOf(order.getDeliveryDate().atStartOfDay()));
        productStatement.setString(++index, order.getDeliveryAddress());
        productStatement.setString(++index, order.getPaymentMethod().name());
        productStatement.setString(++index, order.getSessionId());
    }

    private Order buildOrder(ResultSet resultSet) throws SQLException {
        return Order.builder()
                .id(resultSet.getLong(DBOrderColumn.ID_COLUMN.getValue()))
                .secureId(resultSet.getString(DBOrderColumn.SECURE_ID_COLUMN.getValue()))
                .subtotalCost(resultSet.getBigDecimal(DBOrderColumn.SUBTOTAL_COST_COLUMN.getValue()))
                .deliveryCost(resultSet.getBigDecimal(DBOrderColumn.DELIVERY_COST_COLUMN.getValue()))
                .firstName(resultSet.getString(DBOrderColumn.FIRST_NAME_COLUMN.getValue()))
                .lastName(resultSet.getString(DBOrderColumn.LAST_NAME_COLUMN.getValue()))
                .phone(resultSet.getString(DBOrderColumn.PHONE_COLUMN.getValue()))
                .deliveryDate(resultSet.getTimestamp(DBOrderColumn.DELIVERY_DATE_COLUMN.getValue())
                        .toLocalDateTime().toLocalDate())
                .deliveryAddress(resultSet.getString(DBOrderColumn.DELIVERY_ADDRESS_COLUMN.getValue()))
                .paymentMethod(PaymentMethod.valueOf(resultSet.getString(DBOrderColumn.PAYMENT_METHOD_COLUMN.getValue())))
                .build();
    }

    private List<Order> buildOrders(ResultSet resultSet) throws SQLException {
        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            orders.add(buildOrder(resultSet));
        }
        return orders;
    }

    private void validateId(Long id) {
        if (id <= 0) {
            log.error(ERROR_MESSAGE);
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

    private void validateSecureId(String secureId) {
        if (secureId == null || secureId.isBlank()) {
            log.error(ERROR_MESSAGE);
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }
}
