package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.utility.TextUtil;
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
        }

        return false;
    }

}
