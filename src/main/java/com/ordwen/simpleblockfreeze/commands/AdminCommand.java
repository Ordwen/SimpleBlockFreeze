package com.ordwen.simpleblockfreeze.commands;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.configuration.Configuration;
import com.ordwen.simpleblockfreeze.enums.Messages;
import com.ordwen.simpleblockfreeze.enums.Permissions;
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
            plugin.reloadConfig();
            new Configuration(plugin).load();
            final String msg = Messages.CONFIG_RELOADED.toString();
            if (!msg.isEmpty()) sender.sendMessage(msg);
            return true;
        }

        if (!(sender instanceof Player)) {
            playerOnly(sender);
            return false;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            final Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                final String msg = Messages.PLAYER_NOT_FOUND.toString();
                if (!msg.isEmpty()) sender.sendMessage(msg);
                return true;
            }

            target.getInventory().addItem(Configuration.getItem());
            final String msg = Messages.ITEM_GIVEN.toString();
            if (!msg.isEmpty()) sender.sendMessage(msg.replace("{player}", target.getName()));
            return true;
        }

        help(sender);
        return true;
    }
}
