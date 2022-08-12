package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.menu.menus.GameSelector;
import me.MrIronMan.drawit.menu.menus.SpectateMenu;
import me.MrIronMan.drawit.menu.menus.TeleporterMenu;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand extends SubCommand {

    public MenuCommand() {
        super("menu");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length <= 0) {
            player.sendMessage("Error: Unknown");
            return;
        }

        if (args[0].equalsIgnoreCase("games")) {
            new GameSelector(DrawIt.getPlayerMenuUtility(player)).open();
        }

        if (args[0].equalsIgnoreCase("Spectate")) {
            new SpectateMenu(DrawIt.getPlayerMenuUtility(player)).open();
        }

        if (args[0].equalsIgnoreCase("Teleporter")) {
            if (!DrawIt.getInstance().isInGame(player)) {
                player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NOT_IN_GAME)));
                return;
            }
            Game game = DrawIt.getInstance().getGame(player);
            if (game.isSpectator(player.getUniqueId())) {
                new TeleporterMenu(DrawIt.getPlayerMenuUtility(player), game).open();
            } else player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NOT_SPECTATING)));
        }
    }

}
