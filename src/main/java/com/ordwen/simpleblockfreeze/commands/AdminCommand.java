package com.ordwen.simpleblockfreeze.commands;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.enums.Messages;
import com.ordwen.simpleblockfreeze.enums.Permissions;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminCommand extends CommandMessages implements CommandExecutor {

    private final SimpleBlockFreeze plugin;

    public AdminCommand(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(Permissions.ADMIN.toString())) {
            noPermission(sender);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.onReload();
            Messages.CONFIG_RELOADED.send(sender);
            return true;
        }

        if(args.length == 11 && args[0].equalsIgnoreCase("forcesave")) {

            plugin.getBlockManager().saveAsync().thenAccept(ignored -> {
                Messages.CONFIG_RELOADED.send(sender);
            }).exceptionally(throwable -> {
                PluginLogger.error("An error occurred while saving block(s).", throwable);
                Messages.ERROR_OCCURRED.send(sender);
                return null;
            });

            return true;
        }

        if (!(sender instanceof Player)) {
            playerOnly(sender);
            return false;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            final Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                Messages.PLAYER_NOT_FOUND.send(sender);
                return true;
            }

            target.getInventory().addItem(plugin.getItemManager().getItemStack());
            Messages.ITEM_GIVEN.send(sender);
            return true;
        }

        help(sender);
        return true;
    }
}
