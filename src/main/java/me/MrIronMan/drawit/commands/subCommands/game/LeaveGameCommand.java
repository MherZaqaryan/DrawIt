package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.utility.OtherUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LeaveGameCommand extends SubCommand {

    public LeaveGameCommand() {
        super("leave");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!DrawIt.getInstance().isInGame(player)) {
            OtherUtils.returnLobby(player);
            return;
        }

        Game game = DrawIt.getInstance().getGame(player);
        DrawIt.getInstance().activateLobbySettings(player);
        if (game.isSpectator(player.getUniqueId())) {
            return;
        }

        game.getGameManager().leaveGame(player);
    }

}
