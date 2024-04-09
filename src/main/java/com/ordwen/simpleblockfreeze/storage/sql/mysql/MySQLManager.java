package com.ordwen.simpleblockfreeze.storage.sql.mysql;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import com.ordwen.simpleblockfreeze.storage.sql.ManageLocation;
import com.ordwen.simpleblockfreeze.storage.sql.SearchLocation;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.SQLException;

public class MySQLManager extends SQLManager {

    /* init variables */

    // database settings
    private String host;
    private String dbName;
    private String password;
    private String user;
    private String port;

    // instances
    private final FileConfiguration config;

    /**
     * Constructor.
     *
     * @param plugin instance of SimpleBlockFreeze.
     */
    public MySQLManager(SimpleBlockFreeze plugin) {
        this.config = plugin.getConfig();

        super.searchLocation = new SearchLocation(this);
        super.manageLocation = new ManageLocation(this);

        setupDatabase();
    }

    /**
     * Load identifiers for database connection.
     */
    public void initCredentials() {
        final ConfigurationSection section = config.getConfigurationSection("database");
        if (section == null) {
            PluginLogger.error("Database section not found in config.yml.");
            throw new IllegalArgumentException("Database section not found in config.yml.");
        }

        host = section.getString("host");
        dbName = section.getString("name");
        password = section.getString("password");
        user = section.getString("user");
        port = section.getString("port");
    }

    /**
     * Connect to database.
     */
    public void initHikariCP() {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setJdbcUrl(this.toUri());
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.setMaxLifetime(300000L);
        hikariConfig.setLeakDetectionThreshold(10000L);
        hikariConfig.setConnectionTimeout(10000L);

        super.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    /**
     * Init database.
     */
    public void setupDatabase() {
        initCredentials();
        initHikariCP();

        try {
            testConnection();
        } catch (SQLException e) {
            PluginLogger.error(e.getMessage());
        }

        setupTables();
    }

    /**
     * Setup JdbcUrl.
     *
     * @return JdbcUrl.
     */
    private String toUri() {
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dbName;
    }

    @Override
    public String getStorageMode() {
        return "mysql";
    }
}
