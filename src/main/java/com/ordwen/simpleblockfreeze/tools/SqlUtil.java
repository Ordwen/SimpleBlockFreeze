package com.ordwen.simpleblockfreeze.tools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUtil {

    private SqlUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check if a table exists in database.
     *
     * @param connection connection to check.
     * @param tableName  name of the table to check.
     * @return true if table exists.
     * @throws SQLException SQL errors.
     */
    public static boolean tableExists(Connection connection, String tableName) throws SQLException {
        final DatabaseMetaData meta = connection.getMetaData();
        final ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});

        return resultSet.next();
    }
}
