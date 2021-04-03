package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.TextUtil;
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
                player.sendMessage(TextUtil.colorize(MessagesData.NOT_IN_GAME));
            }
        }else {
            player.sendMessage(TextUtil.colorize(MessagesData.NO_PERM_SKIP));
        }
        return true;
    }

}
