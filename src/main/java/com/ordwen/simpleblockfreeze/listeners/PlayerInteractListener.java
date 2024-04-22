package com.ordwen.simpleblockfreeze.listeners;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.enums.Messages;
import com.ordwen.simpleblockfreeze.enums.Permissions;
import com.ordwen.simpleblockfreeze.flag.FlagType;
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

    private final SimpleBlockFreeze plugin;

    public PlayerInteractListener(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null) return;
        if (!plugin.getItemManager().similar(item)) return;

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

        if(!plugin.getFlagManager().test(FlagType.FREEZE_BLOCK, player, location)) {
            Messages.UNAUTHORIZED_REGION.send(player);
            return;
        }

        final Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK) {
            unfreeze(player, block);
        } else if (action == Action.LEFT_CLICK_BLOCK) {
            freeze(player, block);
        }
    }

    private void unfreeze(Player player, Block block) {

        final boolean store = plugin.getBlockManager().isFreezeBlock(block);

        if(!store) {
            Messages.FREEZE_NOT_FOUND.send(player);
            return;
        }

        plugin.getBlockManager().unfreezeBlock(block);
        Messages.UNFREEZE_SUCCESS.send(player);
    }

    private void freeze(Player player, Block block) {

        final boolean store = plugin.getBlockManager().isFreezeBlock(block);

        if(store) {
            Messages.ALREADY_FROZEN.send(player);
            return;
        }

        plugin.getBlockManager().freezeBlock(block);
        Messages.FREEZE_SUCCESS.send(player);
    }
}
