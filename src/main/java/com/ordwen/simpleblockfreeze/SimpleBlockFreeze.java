package com.ordwen.simpleblockfreeze;

import com.ordwen.simpleblockfreeze.configuration.MessagesFile;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import com.ordwen.simpleblockfreeze.storage.sql.h2.H2Manager;
import com.ordwen.simpleblockfreeze.storage.sql.mysql.MySQLManager;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleBlockFreeze extends JavaPlugin {

    public static SimpleBlockFreeze INSTANCE;

    private SQLManager sqlManager;

    @Override
    public void onEnable() {
        PluginLogger.info("Plugin is starting...");
        INSTANCE = this;

        /* init files */
        new MessagesFile(this).loadMessagesFiles();

        /* init storage */
        final String storageMode = getConfig().getString("storage_mode");
        if (storageMode == null) {
            PluginLogger.error("Storage mode not found in config.yml.");
            throw new RuntimeException("Storage mode not found in config.yml.");
        }

        if (storageMode.equalsIgnoreCase("mysql")) {
            PluginLogger.info("MySQL storage mode enabled.");
            sqlManager = new MySQLManager(this);
        } else if (storageMode.equalsIgnoreCase("h2")) {
            PluginLogger.info("H2 storage mode enabled.");
            sqlManager = new H2Manager();
        } else {
            PluginLogger.error("Invalid storage mode in config.yml.");
            throw new RuntimeException("Invalid storage mode in config.yml.");
        }
    }

    @Override
    public void onDisable() {
        PluginLogger.info("Plugin is stopping...");
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }
}
