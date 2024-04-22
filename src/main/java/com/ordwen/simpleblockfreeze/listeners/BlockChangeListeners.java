package com.ordwen.simpleblockfreeze.listeners;

import com.ordwen.simpleblockfreeze.Options;
import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BlockChangeListeners implements Listener {

    private final SimpleBlockFreeze plugin;

    public BlockChangeListeners(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
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

        return plugin.getBlockManager().isFreezeBlock(block);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        if (isBlockFrozen(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {

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
        return Options.VERTICAL_BLOCKS.contains(newMaterial) ? newBlock.getRelative(0, -1, 0) : newBlock;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.getNewState().getType().equals(Material.VINE)) return;

        if (isBlockFrozen(event.getSource())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (isBlockFrozen(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPhysics(EntityChangeBlockEvent event) {
        if (isBlockFrozen(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFluidFlow(BlockFromToEvent event) {
        if (isBlockFrozen(event.getToBlock())) {
            event.setCancelled(true);
        }
    }
}
