package com.ordwen.simpleblockfreeze.events;

import com.ordwen.simpleblockfreeze.configuration.Configuration;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
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
        if (event.isCancelled()) return;

        if (isBlockFrozen(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        if (event.isCancelled()) return;
        if (event.getNewState().getType().equals(Material.VINE)) return;

        System.out.println("===================================");
        System.out.println("Block GROW Event");
        System.out.println("old: " + event.getBlock().getType());
        System.out.println("old coord: " + event.getBlock().getLocation());
        System.out.println("new: " + event.getNewState().getType());
        System.out.println("new coord: " + event.getNewState().getLocation());

        // Block under = block.getRelative(0, -1, 0);

        if (isBlockFrozen(event.getNewState().getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.isCancelled()) return;
        if (event.getNewState().getType().equals(Material.VINE)) return;

        if (isBlockFrozen(event.getSource())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        if (isBlockFrozen(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPhysics(EntityChangeBlockEvent event) {
        if (event.isCancelled()) return;

        if (isBlockFrozen(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFluidFlow(BlockFromToEvent event) {
        if (event.isCancelled()) return;

        if (isBlockFrozen(event.getToBlock())) {
            event.setCancelled(true);
        }
    }
}
