package me.MrIronMan.drawit.commands.subCommands.setup;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.commands.SubCommand;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.menu.menus.SaveGameMenu;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCommand extends SubCommand {

    public SaveCommand() {
        super("save");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!DrawIt.getInstance().isInSetup(player)) {
            player.sendMessage(TextUtil.colorize(PluginMessages.NOT_IN_SETUP));
            return;
        }
        new SaveGameMenu(DrawIt.getPlayerMenuUtility(player)).open();
    }

}
