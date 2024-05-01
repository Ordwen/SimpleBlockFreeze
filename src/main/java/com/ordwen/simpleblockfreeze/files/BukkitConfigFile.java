package com.ordwen.simpleblockfreeze.files;

import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class BukkitConfigFile {

    private final String filePath;
    private final JavaPlugin plugin;

    private File file;
    private YamlConfiguration configuration;

    public BukkitConfigFile(String filePath, JavaPlugin plugin) {
        this.filePath = filePath;
        this.plugin = plugin;
    }

    public BukkitConfigFile(File folder, String fileName, JavaPlugin plugin) {
        if (!folder.exists()) folder.mkdirs();
        this.filePath = fileName;
        this.plugin = plugin;
    }

    public void setup() {
        if (file == null) {
            this.file = new File(plugin.getDataFolder(), filePath);
        }

        if (!file.exists()) {
            this.plugin.saveResource(filePath, false);
        }
    }

    public void save() throws IOException {
        if (file == null || configuration == null) {
            return;
        }

        this.configuration.save(this.file);
    }

    public YamlConfiguration getConfiguration() {
        if (this.configuration == null) {
            reload();
        }

        return this.configuration;
    }

    public void reload() {
        if (this.file == null) {
            file = new File(plugin.getDataFolder(), filePath);
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.file);
        final InputStream defaultStream = plugin.getResource(filePath);

        if (defaultStream != null) {
            try (InputStreamReader reader = new InputStreamReader(defaultStream)) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
                this.configuration.setDefaults(defaultConfig);
            } catch (IOException exception) {
                PluginLogger.error("An error occurred while saving file at path " + filePath, exception);
            }
        }
    }

    public void updateConfig() {
        updateConfig(Collections.emptyList());
    }

    public void updateConfig(List<String> ignoredPaths) {
        if (!this.file.exists()) {
            setup();
            return;
        }

        final YamlConfiguration configuration = this.getConfiguration();
        InputStream defaultFileStream = plugin.getResource(this.filePath);

        if (defaultFileStream == null) {
            return;
        }

        final YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultFileStream));
        final boolean modified = this.mergeConfigs(defaultConfig, configuration, ignoredPaths);

        if (modified) {
            try {
                save();
                plugin.getLogger().info("File " + filePath + " has been updated to newest version.");
            } catch (IOException exception) {
                plugin.getLogger().log(Level.SEVERE, "Could not save updated configuration.", exception);
            }
        }
    }

    private boolean mergeConfigs(FileConfiguration source, FileConfiguration target, List<String> ignoredPaths) {
        boolean modified = false;

        keyLoop:
        for (String key : source.getKeys(true)) {

            for (String ignored : ignoredPaths) {
                if (key.startsWith(ignored)) {
                    continue keyLoop;
                }
            }

            if (!target.isSet(key)) {
                target.set(key, source.get(key));
                modified = true;
            }
        }

        return modified;
    }
}
