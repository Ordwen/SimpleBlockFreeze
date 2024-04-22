package com.ordwen.simpleblockfreeze;

import com.ordwen.simpleblockfreeze.block.BlockManager;
import com.ordwen.simpleblockfreeze.commands.AdminCommand;
import com.ordwen.simpleblockfreeze.flag.FlagManager;
import com.ordwen.simpleblockfreeze.item.ItemManager;
import com.ordwen.simpleblockfreeze.listeners.BlockChangeListeners;
import com.ordwen.simpleblockfreeze.listeners.PlayerInteractListener;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.ordwen.simpleblockfreeze.tools.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleBlockFreeze extends JavaPlugin {

    private final BlockManager blockManager;
    private final ItemManager itemManager;
    private final FlagManager flagManager;

    public SimpleBlockFreeze() {
        this.itemManager = new ItemManager();
        this.blockManager = new BlockManager(this);
        this.flagManager = new FlagManager(this);
    }

    public static SimpleBlockFreeze getInstance() {
        return JavaPlugin.getPlugin(SimpleBlockFreeze.class);
    }

    @Override
    public void onLoad() {
        getFlagManager().load();
    }

    @Override
    public void onEnable() {

        /* register commands */
        getCommand("sbfadmin").setExecutor(new AdminCommand(this));

        /* register events */
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockChangeListeners(this), this);
    }

    @Override
    public void onDisable() { }

    public void onReload() {
        reloadConfig();
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
