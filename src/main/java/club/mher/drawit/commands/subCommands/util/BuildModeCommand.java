package club.mher.drawit.commands.subCommands.util;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
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
