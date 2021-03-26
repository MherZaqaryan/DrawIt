package me.MrIronMan.drawit.commands.subCommands.setup;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.menuSystem.menus.SaveGameMenu;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length == 0) {
            if (DrawIt.getInstance().isInSetup(player)) {
                new SaveGameMenu(DrawIt.getPlayerMenuUtility(player)).open();
            }else {
                player.sendMessage(TextUtil.colorize(MessagesUtils.NOT_IN_SETUP));
            }
        }

        return false;
    }

}
