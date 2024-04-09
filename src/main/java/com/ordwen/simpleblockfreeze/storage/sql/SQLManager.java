package com.ordwen.simpleblockfreeze.storage.sql;

import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;

import java.sql.*;

public abstract class SQLManager implements ISQLManager {

    protected HikariDataSource hikariDataSource;

    protected SearchLocation searchLocation;
    protected ManageLocation manageLocation;

    public void setupTables() {
        final Connection connection = getConnection();
        try (connection) {
            if (!tableExists(connection, "SBF_LOCATIONS")) {

                final String str = """
                        create table SBF_LOCATIONS
                          (
                             ID int auto_increment  ,
                             WORLD_NAME char(32)  not null  ,
                             X decimal not null, \s
                             Y decimal not null,\s
                             Z decimal not null,\s
                             constraint SBF_PK_LOCATIONS primary key (ID)
                          );""";

                final PreparedStatement preparedStatement = connection.prepareStatement(str);
                preparedStatement.execute();

                preparedStatement.close();
                PluginLogger.info("Table 'SBF_LOCATIONS' created in database.");
            }
        } catch (SQLException e) {
            PluginLogger.error("An error occurred while creating the table 'SBF_LOCATIONS'.");
            PluginLogger.error(e.getMessage());
        }
    }

    /**
     * Check if a table exists in database.
     *
     * @param connection connection to check.
     * @param tableName  name of the table to check.
     * @return true if table exists.
     * @throws SQLException SQL errors.
     */
    private static boolean tableExists(Connection connection, String tableName) throws SQLException {
        final DatabaseMetaData meta = connection.getMetaData();
        final ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});

        return resultSet.next();
    }

    /**
     * Close database connection.
     */
    public void close() {
        this.hikariDataSource.close();
    }

    /**
     * Get database connection.
     *
     * @return database Connection.
     */
    public Connection getConnection() {
        if (this.hikariDataSource != null && !this.hikariDataSource.isClosed()) {
            try {
                return this.hikariDataSource.getConnection();
            } catch (SQLException e) {
                PluginLogger.error("An error occurred while getting a connection to the database.");
                PluginLogger.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Test database connection.
     *
     * @throws SQLException SQL errors.
     */
    protected void testConnection() throws SQLException {
        final Connection con = getConnection();
        if (con.isValid(1)) {
            PluginLogger.info("Plugin successfully connected to database " + con.getCatalog() + ".");
            con.close();
        } else PluginLogger.error("IMPOSSIBLE TO CONNECT TO DATABASE");
    }

    /**
     * Search for a block location in database.
     *
     * @param world world name.
     * @param x     x coordinate.
     * @param y     y coordinate.
     * @param z     z coordinate.
     * @return true if the location was found, false otherwise.
     */
    public boolean searchLocation(String world, double x, double y, double z) {
        return searchLocation.searchLocation(world, x, y, z);
    }

    /**
     * Save a block location in database.
     *
     * @param player involved player.
     * @param world  world name.
     * @param x      x coordinate.
     * @param y      y coordinate.
     * @param z      z coordinate.
     */
    public void saveLocation(Player player, String world, double x, double y, double z) {
        manageLocation.saveLocation(player, world, x, y, z);
    }

    /**
     * Remove a block location from database.
     *
     * @param player involved player.
     * @param world  world name.
     * @param x      x coordinate.
     * @param y      y coordinate.
     * @param z      z coordinate.
     */
    public void deleteLocation(Player player, String world, double x, double y, double z) {
        manageLocation.deleteLocation(player, world, x, y, z);
    }
}
