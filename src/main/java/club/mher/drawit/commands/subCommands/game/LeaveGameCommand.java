package club.mher.drawit.commands.subCommands.game;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.game.Game;
import club.mher.drawit.utility.OtherUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LeaveGameCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            DrawIt.getInstance().activateLobbySettings(player);
            if (game.isSpectator(uuid)) return true;
            game.getGameManager().leaveGame(player);
        }else {
            OtherUtils.returnLobby(player);
        }
        return true;
    }

}
