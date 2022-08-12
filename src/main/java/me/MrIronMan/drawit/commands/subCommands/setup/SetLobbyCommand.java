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

public class SetLobbyCommand extends SubCommand {

    public SetLobbyCommand() {
        super("setlobby");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (!DrawIt.getInstance().isInSetup(player)) {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERMS)));
            return;
        }

        SetupGame setupGame = DrawIt.getInstance().getSetupGame(player);
        setupGame.setLobbyLocation(player.getLocation());
        player.sendMessage(setupGame.customize("{game} &aYou have successfully set lobby location."));
        PluginMessages.sendMessage(player, setupGame.getCurrentMessage());
    }

}
