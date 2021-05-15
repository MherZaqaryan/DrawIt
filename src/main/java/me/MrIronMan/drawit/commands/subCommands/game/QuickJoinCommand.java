package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuickJoinCommand extends SubCommand {

    public QuickJoinCommand() {
        super("quickjoin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        DrawIt.getInstance().quickJoinGame(player);
    }

}
