package com.ordwen.simpleblockfreeze.events;

import com.ordwen.simpleblockfreeze.configuration.Configuration;
import com.ordwen.simpleblockfreeze.enums.Messages;
import com.ordwen.simpleblockfreeze.enums.Permissions;
import com.ordwen.simpleblockfreeze.storage.IBlockManager;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final IBlockManager blockManager;

    public PlayerInteractListener() {
        this.blockManager = Configuration.getBlockManager();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null) return;
        if (!item.isSimilar(Configuration.getItem())) return;

        event.setCancelled(true);
        final Player player = event.getPlayer();

        if (!player.hasPermission(Permissions.USE.toString())) {
            Messages.ITEM_NO_PERMISSION.send(player);
            return;
        }

        final Block block = event.getClickedBlock();
        if (block == null) return;

        final Location location = block.getLocation();
        final World world = location.getWorld();
        if (world == null) return;

        if (!Configuration.canBuild(player, world, location)) {
            Messages.UNAUTHORIZED_REGION.send(player);
            return;
        }

        final Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            unfreeze(player, world, block);
        } else if (action == Action.LEFT_CLICK_BLOCK) {
            freeze(player, world, block);
        }
    }

    private void unfreeze(Player player, World world, Block block) {

        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();

        final boolean store = blockManager.searchLocation(world, x, y, z).join(); // TODO: Change this

        if(!store) {
            Messages.FREEZE_NOT_FOUND.send(player);
            return;
        }

        blockManager.deleteLocation(world, x, y, z)
                .thenRun(() -> {
                    Messages.UNFREEZE_SUCCESS.send(player);
                }).exceptionally(exception -> {
                    PluginLogger.error("An error occurred while deleting a block location in the database.");
                    PluginLogger.error(exception.getMessage());
                    Messages.ERROR_OCCURRED.send(player);
                    return null;
                });
    }

    private void freeze(Player player, World world, Block block) {

        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();

        final boolean store = blockManager.searchLocation(world, x, y, z).join(); // TODO: Change this

        if(store) {
            Messages.ALREADY_FROZEN.send(player);
            return;
        }

        blockManager.deleteLocation(world, x, y, z)
                .thenRun(() -> {
                    Messages.FREEZE_SUCCESS.send(player);
                }).exceptionally(exception -> {
                    PluginLogger.error("An error occurred while saving a block location in the database.");
                    PluginLogger.error(exception.getMessage());
                    Messages.ERROR_OCCURRED.send(player);
                    return null;
                });
    }
}
