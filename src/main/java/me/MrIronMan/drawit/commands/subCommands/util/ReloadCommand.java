package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.TextUtil;
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
