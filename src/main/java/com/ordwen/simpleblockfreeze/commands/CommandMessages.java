package com.ordwen.simpleblockfreeze.commands;

import com.ordwen.simpleblockfreeze.enums.Messages;
import org.bukkit.command.CommandSender;

public abstract class CommandMessages {

    /**
     * Sends the player help message to the sender.
     *
     * @param sender the sender.
     */
    protected void help(CommandSender sender) {
        final String msg = Messages.ADMIN_HELP.toString();
        if (!msg.isEmpty()) sender.sendMessage(msg);
    }

    /**
     * Sends the no permission message to the sender.
     *
     * @param sender the sender.
     */
    protected void noPermission(CommandSender sender) {
        final String msg = Messages.COMMAND_NO_PERMISSION.toString();
        if (!msg.isEmpty()) sender.sendMessage(msg);
    }

    /**
     * Sends a message to the sender indicating that the command can only be executed by a player.
     *
     * @param sender the sender.
     */
    protected void playerOnly(CommandSender sender) {
        final String msg = Messages.PLAYER_ONLY.toString();
        if (!msg.isEmpty()) sender.sendMessage(msg);
    }
}
