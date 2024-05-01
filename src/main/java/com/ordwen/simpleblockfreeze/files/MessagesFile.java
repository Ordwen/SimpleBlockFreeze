package com.ordwen.simpleblockfreeze.files;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.enums.Messages;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesFile {

    private final SimpleBlockFreeze plugin;

    private File messagesFile;
    private FileConfiguration messages;

    public MessagesFile(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
    }

    /**
     * Init messages file.
     */
    public void setup() {
        if (messagesFile == null) {
            messagesFile = new File(plugin.getDataFolder(), "messages.yml");

            if (!messagesFile.exists()) {
                plugin.saveResource("messages.yml", false);
                PluginLogger.info("Messages file created.");
            }
        }
    }

    public void reload() {
        messages = new YamlConfiguration();

        /* Messages file */
        try {
            messages.load(messagesFile);
        } catch (InvalidConfigurationException | IOException e) {
            PluginLogger.error("An error occurred on the load of the messages file.", e);
        }

        for (Messages item : Messages.values()) {
            if (messages.getString(item.getPath()) == null) {
                messages.set(item.getPath(), item.getDefault());
            }
        }

        Messages.setFile(messages);

        try {
            messages.save(messagesFile);
        } catch(IOException e) {
            PluginLogger.error("An error happened on the save of the messages file.", e);
        }
    }
}
