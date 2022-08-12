package club.mher.drawit.commands.subCommands.util;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (sender.hasPermission(PermissionsUtil.COMMAND_RELOAD)) {
            DrawIt.getConfigData().reload();
            DrawIt.getMessagesData().reload();
            DrawIt.getWordsData().reload();
            sender.sendMessage(TextUtil.colorize(PluginMessages.CONFIG_RELOADED));
        }else {
            sender.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERMS)));
        }

        return true;
    }

}
