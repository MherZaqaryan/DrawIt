package club.mher.drawit.commands.subCommands.setup;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.game.Game;
import club.mher.drawit.game.SetupGame;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (player.hasPermission(PermissionsUtil.COMMANDS_DELETE)) {
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
