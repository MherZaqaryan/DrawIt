package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.utility.OtherUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveGameCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            game.getGameManager().leaveGame(player);
            DrawIt.getInstance().activateLobbySettings(player);
        }else {
            OtherUtils.returnLobby(player);
        }
        return true;
    }

}
