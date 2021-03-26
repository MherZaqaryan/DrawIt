package me.MrIronMan.drawit.commands.subCommands.setup;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesUtils;
import me.MrIronMan.drawit.game.SetupGame;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand extends SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (DrawIt.getInstance().isInSetup(player)) {
            SetupGame setupGame = DrawIt.getInstance().getSetupGame(player);
            setupGame.setLobbyLocation(player.getLocation());
            player.sendMessage(setupGame.customize("{game} &aYou have successfully set lobby location."));
            MessagesUtils.sendMessage(player, setupGame.getCurrentMessage());
        }else {
            player.sendMessage(TextUtil.colorize(MessagesUtils.NOT_IN_SETUP));
        }

        return true;
    }

}
