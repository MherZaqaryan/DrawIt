package club.mher.drawit.commands.subCommands.game;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.game.Game;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkipGameCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission(PermissionsUtil.COMMAND_SKIP)) {
            if (DrawIt.getInstance().isInGame(player)) {
                Game game = DrawIt.getInstance().getGame(player);
                game.getGameManager().skip(player);
            } else {
                player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NOT_IN_GAME)));
            }
        }else {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERM_SKIP)));
        }
        return true;
    }

}
