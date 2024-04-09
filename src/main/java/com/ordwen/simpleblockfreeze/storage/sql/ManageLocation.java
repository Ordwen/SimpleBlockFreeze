package com.ordwen.simpleblockfreeze.storage.sql;

import com.ordwen.simpleblockfreeze.enums.Messages;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ManageLocation {

    /* instance of SQLManager */
    private final SQLManager sqlManager;

    /**
     * Constructor.
     *
     * @param sqlManager instance of MySQLManager.
     */
    public ManageLocation(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    /**
     * Save player quests progression.
     *
     * @param world world name.
     * @param x     x coordinate.
     * @param y     y coordinate.
     * @param z     z coordinate.
     */
    public void saveLocation(Player player, String world, double x, double y, double z) {
        if (sqlManager.searchLocation(world, x, y, z)) {
            final String msg = Messages.ALREADY_FROZEN.toString();
            if (!msg.isEmpty()) player.sendMessage(msg);
            return;
        }

        final Connection connection = sqlManager.getConnection();
        try (connection) {
            final String SAVE_QUERY = "INSERT INTO SBF_LOCATIONS(WORLD_NAME, X, Y, Z) VALUES (?, ?, ?, ?)";
            runStatement(world, x, y, z, connection, SAVE_QUERY);

            final String msg = Messages.FREEZE_SUCCESS.toString();
            if (!msg.isEmpty()) player.sendMessage(msg);
        } catch (SQLException e) {
            PluginLogger.error("An error occurred while saving a block location in the database.");
            PluginLogger.error(e.getMessage());

            final String msg = Messages.ERROR_OCCURRED.toString();
            if (!msg.isEmpty()) player.sendMessage(msg);
        }
    }

    /**
     * Delete a location from the database.
     *
     * @param world world name.
     * @param x     x coordinate.
     * @param y     y coordinate.
     * @param z     z coordinate.
     */
    public void deleteLocation(Player player, String world, double x, double y, double z) {
        if (!sqlManager.searchLocation(world, x, y, z)) {
            final String msg = Messages.FREEZE_NOT_FOUND.toString();
            if (!msg.isEmpty()) player.sendMessage(msg);
            return;
        }

        final Connection connection = sqlManager.getConnection();
        try (connection) {
            final String DELETE_QUERY = "DELETE FROM SBF_LOCATIONS WHERE WORLD_NAME = ? AND X = ? AND Y = ? AND Z = ?";
            runStatement(world, x, y, z, connection, DELETE_QUERY);

            final String msg = Messages.UNFREEZE_SUCCESS.toString();
            if (!msg.isEmpty()) player.sendMessage(msg);
        } catch (SQLException e) {
            PluginLogger.error("An error occurred while deleting a block location in the database.");
            PluginLogger.error(e.getMessage());

            final String msg = Messages.ERROR_OCCURRED.toString();
            if (!msg.isEmpty()) player.sendMessage(msg);
        }
    }

    /**
     * Get the prepared statement with all the parameters.
     *
     * @param world      world name.
     * @param x          x coordinate.
     * @param y          y coordinate.
     * @param z          z coordinate.
     * @param connection connection to database.
     * @param saveQuery  query to save location.
     */
    private void runStatement(String world, double x, double y, double z, Connection connection, String saveQuery) {
        try (final PreparedStatement playerStatement = connection.prepareStatement(saveQuery)) {
            playerStatement.setString(1, world);
            playerStatement.setDouble(2, x);
            playerStatement.setDouble(3, y);
            playerStatement.setDouble(4, z);

            playerStatement.executeUpdate();
        } catch (SQLException e) {
            PluginLogger.error("An error occurred while preparing the database statement.");
            PluginLogger.error(e.getMessage());
        }
    }
}
