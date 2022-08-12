package club.mher.drawit.commands.subCommands.util;

import club.mher.drawit.DrawIt;
import club.mher.drawit.commands.SubCommand;
import club.mher.drawit.data.MessagesData;
import club.mher.drawit.data.PluginMessages;
import club.mher.drawit.utility.PermissionsUtil;
import club.mher.drawit.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMainLobbyCommand extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission(PermissionsUtil.COMMAND_SETMAINLOBBY)) {
            if (!DrawIt.getInstance().isInGame(player)) {
                DrawIt.getInstance().setLobbyLocation(player.getLocation());
                player.sendMessage(TextUtil.colorize("{prefix} &aLobby location successfully set."));
            }else {
                player.sendMessage(TextUtil.colorize(PluginMessages.SET_LOBBY_IN_GAME));
            }
        }else {
            player.sendMessage(TextUtil.colorize(DrawIt.getMessagesData().getString(MessagesData.NO_PERMS)));
        }
        return true;
    }
}
