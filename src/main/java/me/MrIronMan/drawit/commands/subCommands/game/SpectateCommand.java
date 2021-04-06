package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.menuSystem.menus.TeleporterMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        if (DrawIt.getInstance().isInGame(player)) {
            Game game = DrawIt.getInstance().getGame(player);
            if (game.isSpectator(player.getUniqueId())) {
                if (args[0].equalsIgnoreCase("Teleporter")) {
                    new TeleporterMenu(DrawIt.getPlayerMenuUtility(player), game).open();
                }else if (args[0].equalsIgnoreCase("Leave")) {
                    DrawIt.getInstance().activateLobbySettings(player);
                }
            }
        }

        return true;
    }
}
