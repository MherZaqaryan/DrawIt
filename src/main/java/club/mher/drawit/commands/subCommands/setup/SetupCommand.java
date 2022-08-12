package club.mher.drawit.commands.subCommands.setup;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.game.SetupGame;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        if (player.hasPermission(PermissionsUtil.COMMAND_SETUP)) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("Exit")) {
                    DrawIt.getInstance().exitSetupMode(player);
                } else if (!DrawIt.getInstance().isLobbySet()) {
                    player.sendMessage(TextUtil.colorize(PluginMessages.SETUP_LOBBY_NOT_SET));
                } else if (DrawIt.getInstance().isInSetup(player)) {
                    player.sendMessage(TextUtil.colorize(PluginMessages.ALREADY_IN_SETUP));
                } else {
                    SetupGame setupGame;
                    if (DrawIt.getInstance().getGame(args[0]) != null) {
                        setupGame = new SetupGame(DrawIt.getInstance().getGame(args[0]));
                    } else {
                        setupGame = new SetupGame(args[0]);
                    }
                    DrawIt.getInstance().enableSetupMode(player, setupGame);
                    PluginMessages.sendMessage(player, setupGame.getCurrentMessage());
                }
            } else {
                player.sendMessage(TextUtil.colorize(PluginMessages.USAGE_COMMAND_SETUP));
            }
        }else {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERMS)));
        }
        return true;
    }

}
