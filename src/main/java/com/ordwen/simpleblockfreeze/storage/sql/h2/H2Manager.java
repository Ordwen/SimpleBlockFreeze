package com.ordwen.simpleblockfreeze.storage.sql.h2;

import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.Contract;

public class H2Manager extends SQLManager {

    public H2Manager() {
        this(initH2());
    }

    public H2Manager(HikariDataSource hikariDataSource) {
        super(hikariDataSource);
    }

    @Contract("-> new")
    private static HikariDataSource initH2() {
        final HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:./plugins/SimpleBlockFreeze/database");
        config.setUsername("odq");
        config.setPassword("");
        config.setMaximumPoolSize(100);
        config.setMaxLifetime(300000L);
        config.setLeakDetectionThreshold(60000L);
        config.setConnectionTimeout(60000L);
        return new HikariDataSource(config);
    }
}
