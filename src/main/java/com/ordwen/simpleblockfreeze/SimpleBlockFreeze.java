package com.ordwen.simpleblockfreeze;

import com.ordwen.simpleblockfreeze.commands.AdminCommand;
import com.ordwen.simpleblockfreeze.configuration.Configuration;
import com.ordwen.simpleblockfreeze.configuration.MessagesFile;
import com.ordwen.simpleblockfreeze.events.BlockChangeListeners;
import com.ordwen.simpleblockfreeze.events.PlayerInteractListener;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleBlockFreeze extends JavaPlugin {

    public static SimpleBlockFreeze INSTANCE;

    @Override
    public void onEnable() {
        PluginLogger.info("Plugin is starting...");
        INSTANCE = this;

        /* init files */
        new MessagesFile(this).loadMessagesFiles();

        /* load config */
        new Configuration(this).load();

        /* register commands */
        getCommand("sbfadmin").setExecutor(new AdminCommand());

        /* register events */
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BlockChangeListeners(), this);
    }

    @Override
    public void onDisable() {
        PluginLogger.info("Plugin is stopping...");
    }
}
