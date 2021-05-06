package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.GameState;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStartCommand extends SubCommand {
    public ForceStartCommand() {
        super("forcestart");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
            if (DrawIt.getInstance().isInGame(player)) {
                Game game = DrawIt.getInstance().getGame(player);
                if (game.isGameState(GameState.WAITING)) {
                    game.getGameManager().setForce();
                    game.getGameManager().startCountdown();
                }
            }
    }
}
