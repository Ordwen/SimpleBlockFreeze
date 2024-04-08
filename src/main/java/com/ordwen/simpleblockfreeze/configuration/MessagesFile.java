package com.ordwen.simpleblockfreeze.configuration;

import com.ordwen.simpleblockfreeze.Messages;
import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesFile {

    private final SimpleBlockFreeze plugin;

    public MessagesFile(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
    }

    /**
     * Init messages files.
     */
    public void loadMessagesFiles() {
        final File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        final FileConfiguration messages = new YamlConfiguration();

        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
            PluginLogger.info("Messages file created.");
        }

        try {
            messages.load(messagesFile);
        } catch (InvalidConfigurationException | IOException e) {
            PluginLogger.error("An error occurred on the load of the messages file.");
            PluginLogger.error("If the problem persists, contact the developer.");
            PluginLogger.error(e.getMessage());
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
            PluginLogger.error("An error happened on the save of the messages file.");
            PluginLogger.error("If the problem persists, contact the developer.");
            PluginLogger.error(e.getMessage());
        }

        PluginLogger.fine("Messages file successfully loaded.");
    }
}
