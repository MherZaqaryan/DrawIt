package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMainLobbyCommand extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        DrawIt.getInstance().setLobbyLocation(player.getLocation());
        player.sendMessage(TextUtil.colorize("{prefix} &aLobby location successfully set."));
        return true;
    }
}
