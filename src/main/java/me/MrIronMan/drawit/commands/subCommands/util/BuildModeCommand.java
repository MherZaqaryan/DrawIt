package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildModeCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (player.hasPermission(PermissionsUtil.COMMAND_BUILD)) {
            if (DrawIt.getBuildMode(player)) {
                player.sendMessage(TextUtil.colorize(PluginMessages.BUILDMODE_DISABLED));
            }else {
                player.sendMessage(TextUtil.colorize(PluginMessages.BUILDMODE_ENABLED));
            }
            DrawIt.setBuildMode(player, !DrawIt.getBuildMode(player));
        }else {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERMS)));
        }

        return true;
    }

}
