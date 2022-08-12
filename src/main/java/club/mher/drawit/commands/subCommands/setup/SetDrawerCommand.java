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

public class SetDrawerCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission(PermissionsUtil.COMMAND_SETUP)) {
            if (DrawIt.getInstance().isInSetup(player)) {
                SetupGame setupGame = DrawIt.getInstance().getSetupGame(player);
                setupGame.setDrawerLocation(player.getLocation());
                player.sendMessage(setupGame.customize("{game} &aYou have successfully set drawer location."));
                PluginMessages.sendMessage(player, setupGame.getCurrentMessage());
            } else {
                player.sendMessage(TextUtil.colorize(PluginMessages.NOT_IN_SETUP));
            }
        }else {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERMS)));
        }
        return true;
    }

}
