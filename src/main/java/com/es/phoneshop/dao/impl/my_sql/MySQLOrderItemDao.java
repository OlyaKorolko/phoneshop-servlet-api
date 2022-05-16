package com.es.phoneshop.dao.impl.my_sql;

import com.es.phoneshop.dao.OrderItemDao;
import com.es.phoneshop.dao.utils.DBConnector;
import com.es.phoneshop.dao.utils.DBQueries;
import com.es.phoneshop.enums.db.DBProductColumn;
import com.es.phoneshop.model.cart.CartItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class MySQLOrderItemDao implements OrderItemDao {
    private final DBConnector connector;

    @Override
    public void save(List<CartItem> cartItems) {
        Long orderId = connector.execute(DBQueries.GET_LAST_INSERTED_ORDER_ID, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(DBProductColumn.LAST_INSERTED_ITEM.getValue());
            } else return -1L;
        });
        connector.execute(buildSaveStatement(cartItems.size()), preparedStatement -> {
            int index = 0;
            for (CartItem c : cartItems) {
                index = setupOrderItemStatement(preparedStatement, c, orderId, index);
            }
            preparedStatement.executeUpdate();
        });
    }

    private String buildSaveStatement(int size) {
        StringBuilder stringBuilder =
                new StringBuilder("insert into order_item (order_id, product_id, quantity) values ")
                        .append("(?,?,?), ".repeat(size));
        return stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length())
                .append(";")
                .toString();
    }

    private int setupOrderItemStatement(PreparedStatement productStatement, CartItem cartItem, Long orderId, int index)
            throws SQLException {
        productStatement.setLong(index + 1, orderId);
        productStatement.setLong(index + 2, cartItem.getProduct().getId());
        productStatement.setInt(index + 3, cartItem.getQuantity());
        return index + 3;
    }

}
