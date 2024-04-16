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
        if (block == null) return false;

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

        final Material newMaterial = event.getNewState().getType();
        final Block newBlock = event.getBlock();

        final Block sourceBlock = getVerticalSource(newMaterial, newBlock);
        if (isBlockFrozen(sourceBlock)) {
            event.setCancelled(true);
        }
    }

    /**
     * Get the source block for vertical blocks.
     *
     * @param newMaterial the new material
     * @param newBlock    the new block
     * @return the source block
     */
    private Block getVerticalSource(Material newMaterial, Block newBlock) {
        if (Configuration.containsVerticalBlock(newMaterial)) {
            return newBlock.getRelative(0, -1, 0);
        } else return newBlock;
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
