package com.ordwen.simpleblockfreeze.tools;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AutoUpdater {

    private final SimpleBlockFreeze plugin;

    public AutoUpdater(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdate() {

        final FileConfiguration configFile = new YamlConfiguration();
        final File file = new File(plugin.getDataFolder(), "config.yml");

        try {
            configFile.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            PluginLogger.error("An error occurred while loading the config file.");
            PluginLogger.error(e.getMessage());
        }

        final String currentVersion = plugin.getDescription().getVersion();
        final String configVersion = configFile.getString("version");
        if (configVersion == null) {
            PluginLogger.error("The 'version' field is missing from the config file. The auto updater will not work.");
            return;
        }

        if (!configVersion.equals(currentVersion)) {
            PluginLogger.warn("It looks like you were using an older version of the plugin.");
            PluginLogger.warn("If necessary, your files will be automatically updated.");
        }

        // update the config version
        configFile.set("version", currentVersion);

        try {
            configFile.save(file);
        } catch (IOException e) {
            PluginLogger.error("An error occurred while saving the config file.");
            PluginLogger.error(e.getMessage());
        }
    }
}
