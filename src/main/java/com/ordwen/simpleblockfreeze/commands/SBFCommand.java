package com.ordwen.simpleblockfreeze.commands;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.enums.Messages;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command({"sbf", "simpleblockfreeze"})
public class SBFCommand {

    private final SimpleBlockFreeze plugin;

    public SBFCommand(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
    }

    @Subcommand("help")
    @DefaultFor({"sbf", "simpleblockfreeze"})
    public void onHelp(BukkitCommandActor actor) {
        Messages.ADMIN_HELP.send(actor.getSender());
    }

    @Subcommand("reload")
    @CommandPermission("simpleblockfreeze.command.reload")
    public void onReload(BukkitCommandActor actor) {
        plugin.onReload();
        Messages.CONFIG_RELOADED.send(actor.getSender());
    }

    @Subcommand("forcesave")
    @CommandPermission("simpleblockfreeze.command.forcesave")
    public void onForceSave(BukkitCommandActor actor) {

        plugin.getBlockManager().saveAsync().thenAccept(ignored -> {
            Messages.CONFIG_RELOADED.send(actor.getSender());
        }).exceptionally(throwable -> {
            PluginLogger.error("An error occurred while saving block(s).", throwable);
            Messages.ERROR_OCCURRED.send(actor.getSender());
            return null;
        });

    }

    @Subcommand("give")
    public void onGive(BukkitCommandActor actor, @Optional Player player) {
        final Player target = player != null ? player : actor.requirePlayer();

        target.getInventory().addItem(plugin.getItemManager().getItemStack());
        Messages.ITEM_GIVEN.send(actor.getSender());
    }
}
