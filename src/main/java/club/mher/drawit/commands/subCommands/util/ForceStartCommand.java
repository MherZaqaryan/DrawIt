package club.mher.drawit.commands.subCommands.util;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.game.Game;
import club.mher.drawit.game.GameState;
import club.mher.drawit.utility.PermissionsUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStartCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (player.hasPermission(PermissionsUtil.COMMAND_FORCESTART)) {
            if (DrawIt.getInstance().isInGame(player)) {
                Game game = DrawIt.getInstance().getGame(player);
                if (game.isGameState(GameState.WAITING)) {
                    game.getGameManager().setForce();
                    game.getGameManager().startCountdown();
                }
            }
        }

        return true;
    }

}
