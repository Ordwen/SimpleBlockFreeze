package com.ordwen.simpleblockfreeze.storage.sql;

import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchLocation {

    private final SQLManager sqlManager;

    /**
     * Constructor.
     *
     * @param sqlManager instance of MySQLManager.
     */
    public SearchLocation(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    /**
     * Search for a block location in database.
     *
     * @param world world name.
     * @param x     x coordinate.
     * @param y     y coordinate.
     * @param z     z coordinate.
     */
    public boolean searchLocation(String world, double x, double y, double z) {
        boolean hasStoredData = false;

        try {
            final Connection connection = sqlManager.getConnection();
            final String query = "SELECT SBF_LOCATIONS.ID FROM SBF_LOCATIONS WHERE WORLD_NAME = ? AND X = ? AND Y = ? AND Z = ?";

            final PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, world);
            preparedStatement.setDouble(2, x);
            preparedStatement.setDouble(3, y);
            preparedStatement.setDouble(4, z);

            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                hasStoredData = true;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            PluginLogger.error(ChatColor.RED + "An error occurred while searching for a block location in the database.");
            PluginLogger.error(e.getMessage());
        }

        return hasStoredData;
    }
}