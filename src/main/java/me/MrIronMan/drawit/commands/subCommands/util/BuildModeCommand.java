package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.commands.SubCommand;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildModeCommand extends SubCommand {
    public BuildModeCommand() {
        super("build");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!DrawIt.getBuildMode(player)) {
            player.sendMessage(TextUtil.colorize(PluginMessages.BUILDMODE_ENABLED));
            return;
        }

        player.sendMessage(TextUtil.colorize(PluginMessages.BUILDMODE_DISABLED));
        DrawIt.setBuildMode(player, !DrawIt.getBuildMode(player));
    }

}
