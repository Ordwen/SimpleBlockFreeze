package com.ordwen.simpleblockfreeze.storage.sql;

import com.ordwen.simpleblockfreeze.storage.IBlockManager;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.ordwen.simpleblockfreeze.tools.SqlUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public abstract class SQLManager implements IBlockManager {

    private final HikariDataSource hikariDataSource;

    protected SQLManager(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    @Override
    public void init() {
        setupTables();
    }

    @Override
    public void close() {
        this.hikariDataSource.close();
    }

    @Override
    public CompletableFuture<Void> saveLocation(World world, int x, int y, int z) {

        return CompletableFuture.runAsync(() -> {

            final String SAVE_QUERY = "INSERT INTO SBF_LOCATIONS(WORLD_NAME, X, Y, Z) VALUES (?, ?, ?, ?)";

            try(final Connection connection = getConnection()) {
                runStatement(world, x, y, z, connection, SAVE_QUERY);
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteLocation(World world, int x, int y, int z) {
        return CompletableFuture.runAsync(() -> {

            final String DELETE_QUERY = "DELETE FROM SBF_LOCATIONS WHERE WORLD_NAME = ? AND X = ? AND Y = ? AND Z = ?";

            try (Connection connection = getConnection()) {
                runStatement(world, x, y, z, connection, DELETE_QUERY);
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> searchLocation(World world, int x, int y, int z) {
        return CompletableFuture.supplyAsync(() -> {

            final String SEARCH_QUERY = "SELECT 1 FROM SBF_LOCATIONS WHERE WORLD_NAME = ? AND X = ? AND Y = ? AND Z = ?";

            try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(SEARCH_QUERY)) {

                stmt.setString(1, world.getName());
                stmt.setInt(2, x);
                stmt.setInt(3, y);
                stmt.setInt(4, z);

                try (ResultSet resultSet = stmt.executeQuery()) {
                    return resultSet.next();
                }

            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }


    /**
     * Get database connection.
     *
     * @return database Connection.
     */
    public Connection getConnection() {

        if(this.hikariDataSource == null || this.hikariDataSource.isClosed()) {
            return null;
        }

        try {
            return this.hikariDataSource.getConnection();
        } catch (SQLException e) {
            PluginLogger.error("An error occurred while getting a connection to the database.");
            PluginLogger.error(e.getMessage());
        }

        return null;
    }

    private void setupTables() {
        try (final Connection connection = getConnection()) {

            if(SqlUtil.tableExists(connection, "SBF_LOCATIONS")) {
                return;
            }

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

            try (final PreparedStatement preparedStatement = connection.prepareStatement(str)) {
                preparedStatement.execute();
                PluginLogger.info("Table 'SBF_LOCATIONS' created in database.");
            }

        } catch (SQLException e) {
            PluginLogger.error("An error occurred while creating the table 'SBF_LOCATIONS'.");
            PluginLogger.error(e.getMessage());
        }
    }

    private void runStatement(World world, int x, int y, int z, Connection connection, String query) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, world.getName());
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setInt(4, z);
            stmt.executeUpdate();
        }
    }
}
