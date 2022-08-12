package club.mher.drawit.commands.subCommands.game;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.game.Game;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length == 1) {
            Game game = DrawIt.getInstance().getGame(args[0]);
            if (game != null){
                game.getGameManager().joinGame(player);
            }else {
                player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.GAME_NOT_FOUND)));
            }
        }else {
            player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_JOIN));
        }

        return false;
    }

}
