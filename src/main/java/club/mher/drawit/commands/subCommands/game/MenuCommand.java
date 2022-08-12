package club.mher.drawit.commands.subCommands.game;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.game.Game;
import club.mher.drawit.menu.menus.GameSelector;
import club.mher.drawit.menu.menus.SpectateMenu;
import club.mher.drawit.menu.menus.TeleporterMenu;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("Games")) {
                new GameSelector(DrawIt.getPlayerMenuUtility(player)).open();
            }
            else if (args[0].equalsIgnoreCase("Spectate")) {
                new SpectateMenu(DrawIt.getPlayerMenuUtility(player)).open();
            }
            else if (args[0].equalsIgnoreCase("Teleporter")) {
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
            }else {
                player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_MENU));
            }
            return true;
        }

        return true;
    }

}
