package com.ordwen.simpleblockfreeze.events;

import com.ordwen.simpleblockfreeze.configuration.Configuration;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockStateChangeListener implements Listener {

    private final SQLManager sqlManager;

    public BlockStateChangeListener() {
        this.sqlManager = Configuration.getSQLManager();
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        final Block block = event.getBlock();
        final Location location = block.getLocation();
        final World world = location.getWorld();
        if (world == null) return;

        if (sqlManager.searchLocation(world.getName(), location.getX(), location.getY(), location.getZ())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        final Block block = event.getBlock();
        final Location location = block.getLocation();
        final World world = location.getWorld();
        if (world == null) return;

        if (sqlManager.searchLocation(world.getName(), location.getX(), location.getY(), location.getZ())) {
            event.setCancelled(true);
        }
    }
}
