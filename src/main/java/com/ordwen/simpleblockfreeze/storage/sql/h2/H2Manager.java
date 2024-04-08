package com.ordwen.simpleblockfreeze.storage.sql.h2;

import com.ordwen.simpleblockfreeze.storage.sql.ManageLocation;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import com.ordwen.simpleblockfreeze.storage.sql.SearchLocation;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class H2Manager extends SQLManager {


    public H2Manager() {

        super.searchLocation = new SearchLocation(this);
        super.manageLocation = new ManageLocation(this);

        setupDatabase();
    }

    /**
     * Init database.
     */
    public void setupDatabase() {
        initH2();

        try {
            testConnection();
        } catch (SQLException e) {
            PluginLogger.error(e.getMessage());
        }

        setupTables();
    }

    private void initH2() {
        final HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:./plugins/SimpleBlockFreeze/database");
        config.setUsername("odq");
        config.setPassword("");
        config.setMaximumPoolSize(100);
        config.setMaxLifetime(300000L);
        config.setLeakDetectionThreshold(60000L);
        config.setConnectionTimeout(60000L);
        super.hikariDataSource = new HikariDataSource(config);
    }

    @Override
    public String getStorageMode() {
        return "h2";
    }
}
