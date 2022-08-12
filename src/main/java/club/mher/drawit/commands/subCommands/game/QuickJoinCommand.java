package club.mher.drawit.commands.subCommands.game;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuickJoinCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        DrawIt.getInstance().quickJoinGame(player);
        return true;
    }

}
