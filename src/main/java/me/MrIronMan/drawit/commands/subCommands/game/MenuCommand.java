package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.menuSystem.menus.GameSelector;
import me.MrIronMan.drawit.menuSystem.menus.SpectateMenu;
import me.MrIronMan.drawit.menuSystem.menus.TeleporterMenu;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("Games")) {
            new GameSelector(DrawIt.getPlayerMenuUtility(player)).open();
        }else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("Spectate")) {
                new SpectateMenu(DrawIt.getPlayerMenuUtility(player)).open();
                return true;
            }else if (args[0].equalsIgnoreCase("Teleporter")) {
                if (DrawIt.getInstance().isInGame(player)) {
                    Game game = DrawIt.getInstance().getGame(player);
                    if (game.isSpectator(player.getUniqueId())) {
                        new TeleporterMenu(DrawIt.getPlayerMenuUtility(player), game).open();
                    }else {
                        player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NOT_SPECTATING)));
                    }
                }else {
                    player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NOT_IN_GAME)));
                }
            }
        }

        return true;
    }

}
