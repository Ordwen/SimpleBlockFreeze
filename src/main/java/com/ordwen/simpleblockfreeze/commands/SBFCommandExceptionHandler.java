package com.ordwen.simpleblockfreeze.commands;

import com.ordwen.simpleblockfreeze.enums.Messages;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.exception.BukkitExceptionAdapter;
import revxrsal.commands.bukkit.exception.InvalidPlayerException;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.NoPermissionException;

public class SBFCommandExceptionHandler extends BukkitExceptionAdapter {

    @Override
    public void senderNotPlayer(@NotNull CommandActor actor, @NotNull SenderNotPlayerException exception) {
        Messages.PLAYER_ONLY.send(actor.as(BukkitCommandActor.class).getSender());
    }

    @Override
    public void invalidPlayer(@NotNull CommandActor actor, @NotNull InvalidPlayerException exception) {
        Messages.PLAYER_NOT_FOUND.send(actor.as(BukkitCommandActor.class).getSender());
    }

    @Override
    public void noPermission(@NotNull CommandActor actor, @NotNull NoPermissionException exception) {
        Messages.COMMAND_NO_PERMISSION.send(actor.as(BukkitCommandActor.class).getSender());
    }
}
