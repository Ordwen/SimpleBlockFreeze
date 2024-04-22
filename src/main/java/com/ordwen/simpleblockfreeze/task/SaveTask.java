package com.ordwen.simpleblockfreeze.task;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;

public class SaveTask extends BukkitRunnable {

    private final static long DELAY_IN_SECOND = Duration.ofMinutes(2).toSeconds();

    public static BukkitTask runFor(SimpleBlockFreeze plugin) {
        return new SaveTask(plugin).runTaskTimer(plugin, DELAY_IN_SECOND * 20, DELAY_IN_SECOND * 20);
    }

    private final SimpleBlockFreeze plugin;

    public SaveTask(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getBlockManager().saveAsync().thenAccept(ignored -> {
            PluginLogger.info("Block(s) saved successfully.");
        }).exceptionally(throwable -> {
            PluginLogger.error("An error occurred while saving block(s) asynchronously.", throwable);
            return null;
        });
    }
}
