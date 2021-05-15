package me.MrIronMan.drawit.commands.subCommands.setup;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.api.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.game.Game;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand extends SubCommand {

    public DeleteCommand() {
        super("delete");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length <= 1) {
            Game game = DrawIt.getInstance().getGame(args[0].toLowerCase());

            if (game == null) {
                player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.GAME_NOT_FOUND)));
                return;
            }

            if (game.isEnabled()) {
                player.sendMessage(TextUtil.colorize(PluginMessages.DISABLE_GAME));
                return;
            }

            SetupGame setupGame = new SetupGame(game);
            setupGame.remove();
            player.sendMessage(TextUtil.colorize(PluginMessages.GAME_REMOVED).replace("%game%", game.getName()));
        }
    }
}
