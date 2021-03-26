package me.MrIronMan.drawit.commands.subCommands.setup;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("Exit")) {
                DrawIt.getInstance().exitSetupMode(player);
            }
            else if (!DrawIt.getInstance().isLobbySet()) {
                player.sendMessage(TextUtil.colorize(MessagesUtils.SETUP_LOBBY_NOT_SET));
            }
            else if (DrawIt.getInstance().isInSetup(player)) {
                player.sendMessage(TextUtil.colorize(MessagesUtils.ALREADY_IN_SETUP));
            }
            else {
                SetupGame setupGame;
                if (DrawIt.getInstance().getGame(args[0]) != null) {
                    setupGame = new SetupGame(DrawIt.getInstance().getGame(args[0]));
                }else {
                    setupGame = new SetupGame(args[0]);
                }
                DrawIt.getInstance().enableSetupMode(player, setupGame);
                MessagesUtils.sendMessage(player, setupGame.getCurrentMessage());
            }
        }
        else {
            player.sendMessage(TextUtil.colorize(MessagesUtils.USAGE_COMMAND_SETUP));
        }
        return true;
    }

}
