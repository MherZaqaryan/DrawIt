package me.MrIronMan.drawit.commands.subCommands.game;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends SubCommand {

    public JoinCommand() {
        super("join");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length <= 1) {
            player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_JOIN));
            return;
        }

        Game game = DrawIt.getInstance().getGame(args[0]);

        if (game == null) {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.GAME_NOT_FOUND)));
            return;
        }

        game.getGameManager().joinGame(player);
    }
}
