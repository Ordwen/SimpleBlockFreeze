package com.ordwen.simpleblockfreeze;

import com.ordwen.simpleblockfreeze.commands.AdminCommand;
import com.ordwen.simpleblockfreeze.configuration.Configuration;
import com.ordwen.simpleblockfreeze.configuration.MessagesFile;
import com.ordwen.simpleblockfreeze.events.BlockChangeListeners;
import com.ordwen.simpleblockfreeze.events.PlayerInteractListener;
import com.ordwen.simpleblockfreeze.tools.AutoUpdater;
import com.ordwen.simpleblockfreeze.tools.Metrics;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.ordwen.simpleblockfreeze.tools.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleBlockFreeze extends JavaPlugin {

    private static SimpleBlockFreeze instance;

    @Override
    public void onEnable() {
        SimpleBlockFreeze.setInstance(this);

        /* Load Metrics */
        // https://bstats.org/plugin/bukkit/SimpleBlockFreeze/1234
        //int pluginId = 1234;
        //final Metrics metrics = new Metrics(this, pluginId);

        /* Check for updates */
        new AutoUpdater(this).checkForUpdate();
        //checkForSpigotUpdate();

        /* init files */
        new MessagesFile(this).loadMessagesFiles();

        /* load config */
        new Configuration(this).load();

        /* register commands */
        getCommand("sbfadmin").setExecutor(new AdminCommand(this));

        /* register events */
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new BlockChangeListeners(), this);
    }

    @Override
    public void onDisable() {
        if(Configuration.getBlockManager() != null) {
            Configuration.getBlockManager().close();
        }
    }

    /**
     * Check if an update is available.
     */
    private void checkForSpigotUpdate() {
        PluginLogger.info("Checking for update...");
        new UpdateChecker(this, 100990).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                PluginLogger.info("Plugin is up to date.");
            } else {
                PluginLogger.warn("A new update is available !");
                PluginLogger.warn("Current version : " + this.getDescription().getVersion() + ", Available version : " + version);
                PluginLogger.warn("Please download latest version :");
                PluginLogger.warn("https://www.spigotmc.org/resources/odailyquests.100990/");
            }
        });
    }

    /**
     * Set the instance of the plugin.
     *
     * @param plugin instance of the plugin
     */
    public static void setInstance(SimpleBlockFreeze plugin) {
        instance = plugin;
    }

    /**
     * Get the instance of the plugin.
     *
     * @return SimpleBlockFreeze instance
     */
    public static SimpleBlockFreeze getInstance() {
        return instance;
    }
}
