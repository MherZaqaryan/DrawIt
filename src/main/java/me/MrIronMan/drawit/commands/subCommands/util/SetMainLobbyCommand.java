package me.MrIronMan.drawit.commands.subCommands.util;

import me.MrIronMan.drawit.DrawIt;
import me.MrIronMan.drawit.commands.SubCommand;
import me.MrIronMan.drawit.data.MessagesData;
import me.MrIronMan.drawit.data.PluginMessages;
import me.MrIronMan.drawit.utility.PermissionsUtil;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMainLobbyCommand extends SubCommand {
    public SetMainLobbyCommand() {
        super("setmainlobby");
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (DrawIt.getInstance().isInGame(player)) {
            player.sendMessage(TextUtil.colorize(PluginMessages.SET_LOBBY_IN_GAME));
            return;
        }

        DrawIt.getInstance().setLobbyLocation(player.getLocation());
        player.sendMessage(TextUtil.colorize("{prefix} &aLobby location successfully set."));
    }
}
