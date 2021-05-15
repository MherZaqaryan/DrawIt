package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.commands.SubCommand;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {
    public ReloadCommand() {
        super("reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        DrawIt.getConfigData().reload();
        DrawIt.getMessagesData().reload();
        DrawIt.getWordsData().reload();
        sender.sendMessage(TextUtil.colorize(PluginMessages.CONFIG_RELOADED));
    }

}
