package com.ordwen.simpleblockfreeze.events;

import com.ordwen.simpleblockfreeze.configuration.Configuration;
import com.ordwen.simpleblockfreeze.enums.Messages;
import com.ordwen.simpleblockfreeze.enums.Permissions;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
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

    private final SQLManager sqlManager;

    public PlayerInteractListener() {
        this.sqlManager = Configuration.getSQLManager();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null) return;
        if (!item.isSimilar(Configuration.getItem())) return;

        event.setCancelled(true);

        final Player player = event.getPlayer();
        if (!player.hasPermission(Permissions.USE.toString())) {
            final String msg = Messages.ITEM_NO_PERMISSION.toString();
            if (msg != null) player.sendMessage(msg);
            return;
        }

        final Block block = event.getClickedBlock();
        if (block == null) return;

        final Location location = block.getLocation();
        final World world = location.getWorld();
        if (world == null) return;

        if (!Configuration.canBuild(player, world, location)) {
            final String msg = Messages.UNAUTHORIZED_REGION.toString();
            if (msg != null) player.sendMessage(msg);
            return;
        }

        final Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            sqlManager.deleteLocation(player, world.getName(), location.getX(), location.getY(), location.getZ());
        } else if (action == Action.LEFT_CLICK_BLOCK) {
            sqlManager.saveLocation(player, world.getName(), location.getX(), location.getY(), location.getZ());
        }
    }
}
