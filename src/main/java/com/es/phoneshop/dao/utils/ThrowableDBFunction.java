package com.es.phoneshop.dao.utils;

import java.sql.PreparedStatement;

public interface ThrowableDBFunction<T> {
    T execute(PreparedStatement preparedStatement) throws Exception;
}
