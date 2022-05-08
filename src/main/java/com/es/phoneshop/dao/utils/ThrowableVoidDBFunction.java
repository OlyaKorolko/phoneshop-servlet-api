package com.es.phoneshop.dao.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ThrowableVoidDBFunction {
    void execute(PreparedStatement preparedStatement) throws SQLException;
}
