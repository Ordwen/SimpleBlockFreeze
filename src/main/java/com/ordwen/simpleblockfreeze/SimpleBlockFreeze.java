package com.ordwen.simpleblockfreeze;

import com.ordwen.simpleblockfreeze.commands.AdminCommand;
import com.ordwen.simpleblockfreeze.configuration.MessagesFile;
import com.ordwen.simpleblockfreeze.flag.FlagManager;
import com.ordwen.simpleblockfreeze.storage.StorageMode;
import com.ordwen.simpleblockfreeze.item.ItemManager;
import com.ordwen.simpleblockfreeze.listeners.BlockChangeListeners;
import com.ordwen.simpleblockfreeze.listeners.PlayerInteractListener;
import com.ordwen.simpleblockfreeze.storage.IBlockManager;
import com.ordwen.simpleblockfreeze.tools.AutoUpdater;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.ordwen.simpleblockfreeze.tools.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleBlockFreeze extends JavaPlugin {

    private IBlockManager blockManager;
    private final ItemManager itemManager;
    private final FlagManager flagManager;

    public SimpleBlockFreeze() {
        this.itemManager = new ItemManager();
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
        loadBlockManager();

        /* register commands */
        getCommand("sbfadmin").setExecutor(new AdminCommand(this));

        /* register events */
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockChangeListeners(this), this);
    }

    @Override
    public void onDisable() {
        if(getBlockManager() != null) {
            getBlockManager().close();
        }
    }

    public void onReload() {
        reloadConfig();
        Options.load(getConfig());
        getItemManager().load(this, getConfig());
    }

    public IBlockManager getBlockManager() {
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

    /**
     * Initialize the storage manager depending on the storage mode.
     */
    private void loadBlockManager() {
        final String literal = getConfig().getString("storage_mode");
        final StorageMode mode = StorageMode.find(literal, StorageMode.INTERNAL);
        this.blockManager = mode.createBlockManager(this);
        getBlockManager().init();
    }
}
