package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddTokensCommand extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;



        return true;
    }
}
