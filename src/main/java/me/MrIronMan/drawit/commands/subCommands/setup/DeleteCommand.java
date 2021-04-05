package me.MrIronMan.drawit.commands.subCommands.setup;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission(PermissionsUtil.COMMANDS_REMOVE)) {
            if (args.length == 1) {
                String gameName = args[0];
                if (DrawIt.getInstance().getGame(gameName) != null) {
                    Game game = DrawIt.getInstance().getGame(gameName);
                    if (!game.isEnabled()) {
                        SetupGame setupGame = new SetupGame(game);
                        setupGame.remove();
                        player.sendMessage(TextUtil.colorize(PluginMessages.GAME_REMOVED).replace("%game%", gameName));
                        return true;
                    }else {
                        player.sendMessage(TextUtil.colorize(PluginMessages.DISABLE_GAME));
                    }
                }else {
                    player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.GAME_NOT_FOUND)));
                }
            } else {
                player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_DELETE));
            }
        }else {
            player.sendMessage(TextUtil.colorize(MessagesData.NO_PERMS));
        }

        return true;
    }
}
