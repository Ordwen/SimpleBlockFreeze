package com.ordwen.simpleblockfreeze;

import com.ordwen.simpleblockfreeze.block.BlockManager;
import com.ordwen.simpleblockfreeze.commands.AdminCommand;
import com.ordwen.simpleblockfreeze.flag.FlagManager;
import com.ordwen.simpleblockfreeze.item.ItemManager;
import com.ordwen.simpleblockfreeze.listeners.BlockChangeListeners;
import com.ordwen.simpleblockfreeze.listeners.PlayerInteractListener;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.ordwen.simpleblockfreeze.tools.UpdateChecker;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;

public final class SimpleBlockFreeze extends JavaPlugin {

    private final BlockManager blockManager;
    private final ItemManager itemManager;
    private final FlagManager flagManager;

    private BukkitConfigFile messageFile;

    private BukkitTask saveTask;

    public SimpleBlockFreeze() {
        this.itemManager = new ItemManager();
        this.blockManager = new BlockManager(this);
        this.flagManager = new FlagManager(this);
    }

    public static SimpleBlockFreeze instance() {
        return JavaPlugin.getPlugin(SimpleBlockFreeze.class);
    }

    @Override
    public void onLoad() {
        getFlagManager().load();
    }

    @Override
    public void onEnable() {

        this.saveTask = SaveTask.runFor(this);

        /* register commands */
        registerCommands();

        /* register events */
        registerListeners();

        try {
            getBlockManager().load();
        } catch (IOException exception) {
            PluginLogger.error("An error occurred while loading blocks.", exception);
        }

        this.messageFile = new BukkitConfigFile(getDataFolder(), "messages.yml", this);
        this.messageFile.setup();
    }

    @Override
    public void onDisable() {

        try {
            getBlockManager().save();
        } catch (IOException exception) {
            PluginLogger.error("An error occurred while saving blocks.", exception);
        }

    }

    public void onReload() {
        reloadConfig();
        messageFile.reload();

        Options.load(getConfig());
        getItemManager().load(this, getConfig());
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public FlagManager getFlagManager() {
        return flagManager;
    }

    private void registerCommands() {
        getCommand("sbfadmin").setExecutor(new AdminCommand(this));
    }

    private void registerListeners() {
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerInteractListener(this), this);
        pm.registerEvents(new BlockChangeListeners(this), this);
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
}
