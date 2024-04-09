package com.ordwen.simpleblockfreeze.events;

import com.ordwen.simpleblockfreeze.configuration.Configuration;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockChangeListeners implements Listener {

    private final SQLManager sqlManager;

    public BlockChangeListeners() {
        this.sqlManager = Configuration.getSQLManager();
    }

    /**
     * Check if a block is frozen.
     *
     * @param block the block to check
     * @return true if the block is frozen, false otherwise
     */
    private boolean isBlockFrozen(Block block) {
        final Location location = block.getLocation();
        final World world = location.getWorld();
        if (world == null) return false;

        return sqlManager.searchLocation(world.getName(), location.getX(), location.getY(), location.getZ());
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (isBlockFrozen(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
       if (isBlockFrozen(event.getNewState().getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (isBlockFrozen(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPhysics(EntityChangeBlockEvent event) {
        if (isBlockFrozen(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFluidFlow(BlockFromToEvent event) {
        if (isBlockFrozen(event.getToBlock())) {
            event.setCancelled(true);
        }
    }
}
