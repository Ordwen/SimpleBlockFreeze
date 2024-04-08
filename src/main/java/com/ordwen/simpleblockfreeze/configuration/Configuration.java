package com.ordwen.simpleblockfreeze.configuration;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.configuration.item.ItemGetter;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import com.ordwen.simpleblockfreeze.storage.sql.h2.H2Manager;
import com.ordwen.simpleblockfreeze.storage.sql.mysql.MySQLManager;
import com.ordwen.simpleblockfreeze.tools.ColorConvert;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Configuration {

    private static ItemStack item;
    private static SQLManager sqlManager;

    private final SimpleBlockFreeze plugin;
    private final FileConfiguration config;

    public Configuration(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void load() {
        plugin.saveDefaultConfig();
        loadItem();
        loadSQLManager();
    }

    /**
     * Load the item used to freeze blocks from the configuration file.
     */
    private void loadItem() {
        final ConfigurationSection itemSection = config.getConfigurationSection("item");
        if (itemSection == null) {
            PluginLogger.error("Item section not found in config.yml.");
            throw new RuntimeException("Item section not found in config.yml.");
        }

        final String material = itemSection.getString("material");
        if (material == null) {
            PluginLogger.error("Material not found in item section in config.yml.");
            throw new RuntimeException("Material not found in item section in config.yml.");
        }
        item = new ItemGetter().getItemStackFromMaterial(material, "material");

        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        final String name = itemSection.getString("name");
        if (name != null) itemMeta.setDisplayName(name);

        final List<String> lore = itemSection.getStringList("lore");
        if (!lore.isEmpty()) lore.replaceAll(ColorConvert::convertColorCode);

        itemMeta.setLore(lore);
    }

    /**
     * Initialize the storage manager depending on the storage mode.
     */
    public void loadSQLManager() {
        final String storageMode = config.getString("storage_mode");
        if (storageMode == null) {
            PluginLogger.error("Storage mode not found in config.yml.");
            throw new RuntimeException("Storage mode not found in config.yml.");
        }

        if (storageMode.equalsIgnoreCase("mysql")) {
            PluginLogger.info("MySQL storage mode enabled.");
            sqlManager = new MySQLManager(plugin);
        } else if (storageMode.equalsIgnoreCase("h2")) {
            PluginLogger.info("H2 storage mode enabled.");
            sqlManager = new H2Manager();
        } else {
            PluginLogger.error("Invalid storage mode in config.yml.");
            throw new RuntimeException("Invalid storage mode in config.yml.");
        }
    }

    /**
     * Get the item used to freeze blocks.
     * @return an ItemStack
     */
    public static ItemStack getItem() {
        return item;
    }

    /**
     * Get the SQL manager.
     * @return the SQL manager
     */
    public static SQLManager getSQLManager() {
        return sqlManager;
    }
}
