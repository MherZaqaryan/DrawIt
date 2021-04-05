package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.menuSystem.menus.GameSelector;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("games")) {
            new GameSelector(DrawIt.getPlayerMenuUtility(player)).open();
        }else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("playing")) {
                return true;
            }
        }

        return true;
    }

}
