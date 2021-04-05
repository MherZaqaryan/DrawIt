package me.MrIronMan.drawit.commands.subCommands.setup;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.TextUtil;
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
