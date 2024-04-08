package com.ordwen.simpleblockfreeze.configuration;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import com.ordwen.simpleblockfreeze.storage.sql.h2.H2Manager;
import com.ordwen.simpleblockfreeze.storage.sql.mysql.MySQLManager;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationLoader {

    private final SimpleBlockFreeze plugin;
    private final FileConfiguration config;

    public ConfigurationLoader(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    /**
     * Initialize the storage manager depending on the storage mode.
     *
     * @return an SQLManager instance.
     */
    public SQLManager getSqlManager() {
        final String storageMode = config.getString("storage_mode");
        if (storageMode == null) {
            PluginLogger.error("Storage mode not found in config.yml.");
            throw new RuntimeException("Storage mode not found in config.yml.");
        }

        if (storageMode.equalsIgnoreCase("mysql")) {
            PluginLogger.info("MySQL storage mode enabled.");
            return new MySQLManager(plugin);
        } else if (storageMode.equalsIgnoreCase("h2")) {
            PluginLogger.info("H2 storage mode enabled.");
            return new H2Manager();
        } else {
            PluginLogger.error("Invalid storage mode in config.yml.");
            throw new RuntimeException("Invalid storage mode in config.yml.");
        }
    }
}
