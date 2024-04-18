package com.ordwen.simpleblockfreeze.storage.sql.mysql;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.storage.sql.DatabaseCredentials;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.Contract;

public class MySQLManager extends SQLManager {

    public MySQLManager(SimpleBlockFreeze plugin) {
        this(new DatabaseCredentials(plugin.getConfig().getConfigurationSection("database")));
    }

    public MySQLManager(DatabaseCredentials credentials) {
        super(initHikariCP(credentials));
    }

    @Contract("_ -> new")
    private static HikariDataSource initHikariCP(DatabaseCredentials credentials) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setJdbcUrl(toUri(credentials));
        hikariConfig.setUsername(credentials.getUser());
        hikariConfig.setPassword(credentials.getPassword());
        hikariConfig.setMaxLifetime(300000L);
        hikariConfig.setLeakDetectionThreshold(10000L);
        hikariConfig.setConnectionTimeout(10000L);
        return new HikariDataSource(hikariConfig);
    }

    /**
     * Setup JdbcUrl.
     *
     * @return JdbcUrl.
     */
    private static String toUri(DatabaseCredentials credentials) {
        return "jdbc:mysql://" + credentials.getHost() + ":" + credentials.getPort() + "/" + credentials.getDbName();
    }
}
